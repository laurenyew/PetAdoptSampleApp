/**
 * Copyright Deusty LLC.
**/

#import "YapDatabaseCloudCorePrivate.h"
#import "YapDatabasePrivate.h"
#import "YapDatabaseLogging.h"

/**
 * Define log level for this file: OFF, ERROR, WARN, INFO, VERBOSE
 * See YapDatabaseLogging.h for more information.
**/
#if DEBUG && robbie_hanson
  static const int ydbLogLevel = YDBLogLevelVerbose | YDBLogFlagTrace;
#elif DEBUG
  static const int ydbLogLevel = YDBLogLevelWarning;
#else
  static const int ydbLogLevel = YDBLogLevelWarning;
#endif
#pragma unused(ydbLogLevel)

NSString *const YapDatabaseCloudCoreDefaultPipelineName = @"default";


@implementation YapDatabaseCloudCore
{
	dispatch_queue_t queue;
	void *IsOnQueueKey;
	
	NSURL *baseServerURL;
	NSString *authToken;
	
	NSMutableDictionary *pipelines;
	NSMutableDictionary *pipelineNameAlias;
	
	NSUInteger suspendCount;
}

/**
 * Subclasses MUST implement this method.
 *
 * This method is used when unregistering an extension in order to drop the related tables.
 * 
 * @param registeredName
 *   The name the extension was registered using.
 *   The extension should be able to generated the proper table name(s) using the given registered name.
 * 
 * @param transaction
 *   A readWrite transaction for proper database access.
 * 
 * @param wasPersistent
 *   If YES, then the extension should drop tables from sqlite.
 *   If NO, then the extension should unregister the proper YapMemoryTable(s).
**/
+ (void)dropTablesForRegisteredName:(NSString *)registeredName
                    withTransaction:(YapDatabaseReadWriteTransaction *)transaction
                      wasPersistent:(BOOL)wasPersistent
{
	YDBLogAutoTrace();
	
	sqlite3 *db = transaction->connection->db;
	
	NSArray *tableNames = @[
	  [self pipelineV2TableNameForRegisteredName:registeredName],
	  [self pipelineV3TableNameForRegisteredName:registeredName],
	  [self queueV1TableNameForRegisteredName:registeredName],
	  [self queueV2TableNameForRegisteredName:registeredName],
	  [self mappingTableNameForRegisteredName:registeredName],
	  [self tagTableNameForRegisteredName:registeredName]
	];
	
	for (NSString *tableName in tableNames)
	{
		NSString *dropTable = [NSString stringWithFormat:@"DROP TABLE IF EXISTS \"%@\";", tableName];
		
		int status = sqlite3_exec(db, [dropTable UTF8String], NULL, NULL, NULL);
		if (status != SQLITE_OK)
		{
			YDBLogError(@"Failed dropping table (%@): %d %s", tableName, status, sqlite3_errmsg(db));
		}
	}
}

+ (NSString *)pipelineV2TableNameForRegisteredName:(NSString *)registeredName
{
	return [NSString stringWithFormat:@"cloudcore_pipeline_%@", registeredName]; // OLD version
}

+ (NSString *)pipelineV3TableNameForRegisteredName:(NSString *)registeredName
{
	return [NSString stringWithFormat:@"cloudcore_pipeline3_%@", registeredName];
}

+ (NSString *)queueV1TableNameForRegisteredName:(NSString *)registeredName
{
	return [NSString stringWithFormat:@"cloudcore_queue_%@", registeredName]; // OLD version
}

+ (NSString *)queueV2TableNameForRegisteredName:(NSString *)registeredName
{
	return [NSString stringWithFormat:@"cloudcore_queue2_%@", registeredName];
}

+ (NSString *)tagTableNameForRegisteredName:(NSString *)registeredName
{
	return [NSString stringWithFormat:@"cloudcore_tag_%@", registeredName];
}

+ (NSString *)mappingTableNameForRegisteredName:(NSString *)registeredName
{
	return [NSString stringWithFormat:@"cloudcore_mapping_%@", registeredName];
}

+ (YDBCloudCoreOperationSerializer)defaultOperationSerializer
{
	return ^ NSData* (YapDatabaseCloudCoreOperation *operation){
		return [NSKeyedArchiver archivedDataWithRootObject:operation];
	};
}

+ (YDBCloudCoreOperationDeserializer)defaultOperationDeserializer
{
	return ^ YapDatabaseCloudCoreOperation * (NSData *operationBlob){
		return (operationBlob.length > 0) ? [NSKeyedUnarchiver unarchiveObjectWithData:operationBlob] : nil;
	};
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark Init
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@synthesize versionTag = versionTag;
@synthesize options = options; // Getter is overriden

@dynamic operationSerializer;
@dynamic operationDeserializer;

@dynamic isSuspended;
@dynamic suspendCount;

- (YapDatabaseCloudCoreOptions *)options
{
	return [options copy]; // Our copy must remain immutable
}

- (instancetype)initWithVersionTag:(NSString *)inVersionTag
                           options:(YapDatabaseCloudCoreOptions *)inOptions
{
	if ((self = [super init]))
	{
		versionTag = inVersionTag ? [inVersionTag copy] : @"";
		options = inOptions ? [inOptions copy] : [[YapDatabaseCloudCoreOptions alloc] init];
		
		queue = dispatch_queue_create("YapDatabaseCloudCore", DISPATCH_QUEUE_SERIAL);
		
		IsOnQueueKey = &IsOnQueueKey;
		dispatch_queue_set_specific(queue, IsOnQueueKey, IsOnQueueKey, NULL);
		
		pipelines = [[NSMutableDictionary alloc] initWithCapacity:1];
		
		operationSerializer = [[self class] defaultOperationSerializer];
		operationDeserializer = [[self class] defaultOperationDeserializer];
	}
	return self;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark YapDatabaseExtension Protocol
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Subclasses may OPTIONALLY implement this method.
 * This method is called during the extension registration process to enusre:
 * - the extension is properly configured & ready to be registered.
 * - the extension will support the database with it's current configuration.
 * 
 * Return YES if the class/instance is ready to proceed with registration.
**/
- (BOOL)supportsDatabase:(YapDatabase __unused *)database
withRegisteredExtensions:(NSDictionary __unused *)registeredExtensions
{
	if ([self defaultPipeline] == nil)
	{
		YDBLogError(@"You MUST register a default pipeline BEFORE registering the extension with the database."
		            @" See [YapDatabaseCloudCore registerPipeline:].");
		return NO;
	}
	
	return YES;
}

/**
 * Subclasses MUST implement this method.
 * Returns a proper instance of the YapDatabaseExtensionConnection subclass.
**/
- (YapDatabaseExtensionConnection *)newConnection:(YapDatabaseConnection *)databaseConnection
{
	// Subclasses should override this method,
	// and return an instance of their own subclass.
	
	return [[YapDatabaseCloudCoreConnection alloc] initWithParent:self databaseConnection:databaseConnection];
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark Table Names
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

- (NSString *)pipelineV2TableName // For migration from OLD verion of table
{
	return [[self class] pipelineV2TableNameForRegisteredName:self.registeredName];
}

- (NSString *)pipelineV3TableName
{
	return [[self class] pipelineV3TableNameForRegisteredName:self.registeredName];
}

- (NSString *)pipelineTableName // Always returns latest version
{
	return [self pipelineV3TableName];
}

- (NSString *)queueV1TableName // For migration from OLD verion of table
{
	return [[self class] queueV1TableNameForRegisteredName:self.registeredName];
}

- (NSString *)queueV2TableName
{
	return [[self class] queueV2TableNameForRegisteredName:self.registeredName];
}

- (NSString *)queueTableName // Always returns latest version
{
	return [self queueV2TableName];
}

- (NSString *)tagTableName
{
	return [[self class] tagTableNameForRegisteredName:self.registeredName];
}

- (NSString *)mappingTableName
{
	return [[self class] mappingTableNameForRegisteredName:self.registeredName];
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark General Configuration
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

- (BOOL)setOperationSerializer:(YDBCloudCoreOperationSerializer)serializer
                  deserializer:(YDBCloudCoreOperationDeserializer)deserializer
{
	if (serializer == NULL) return NO;
	if (deserializer == NULL) return NO;
	
	__strong YapDatabase *database = self.registeredDatabase;
	if (database)
	{
		YDBLogWarn(@"The recordChangesSerializerBlock / deserializerBlock MUST be configured"
		           @" BEFORE the extension itself is registered with the database.");
		return NO;
	}
	
	operationSerializer = serializer;
	operationDeserializer = deserializer;
	
	return YES;
}

- (YDBCloudCoreOperationSerializer)operationSerializer
{
	return operationSerializer;
}

- (YDBCloudCoreOperationDeserializer)operationDeserializer
{
	return operationDeserializer;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark Pipelines
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

- (YapDatabaseCloudCorePipeline *)defaultPipeline
{
	return [self pipelineWithName:YapDatabaseCloudCoreDefaultPipelineName];
}

- (YapDatabaseCloudCorePipeline *)pipelineWithName:(NSString *)name
{
	if (name == nil)
		name = YapDatabaseCloudCoreDefaultPipelineName;
	
	__block YapDatabaseCloudCorePipeline *pipeline = nil;
	
	dispatch_block_t block = ^{
	#pragma clang diagnostic push
	#pragma clang diagnostic ignored "-Wimplicit-retain-self"
		
		pipeline = pipelines[name];
		
		if (pipeline == nil)
		{
			NSString *alias = pipelineNameAlias[name];
			if (alias)
			{
				pipeline = pipelines[alias];
			}
		}
		
	#pragma clang diagnostic pop
	};
	
	if (dispatch_get_specific(IsOnQueueKey))
		block();
	else
		dispatch_sync(queue, block);
	
	return pipeline;
}

// For pipeline table:
//
// - you need to know what the real names are
// - and you need to be able to map from previous name to current name

- (BOOL)registerPipeline:(YapDatabaseCloudCorePipeline *)pipeline
{
	if (pipeline == nil) return NO;
	
	__strong YapDatabase *database = self.registeredDatabase;
	if (database)
	{
		YDBLogWarn(@"All pipelines MUST be registered BEFORE the extension itself is registered with the database.");
		return NO;
	}
	
	__block BOOL result = YES;
	
	dispatch_block_t block = ^{ @autoreleasepool {
	#pragma clang diagnostic push
	#pragma clang diagnostic ignored "-Wimplicit-retain-self"
		
		if (pipelines[pipeline.name] != nil)
		{
			result = NO;
			return;
		}
		
		if (![pipeline setOwner:self])
		{
			result = NO;
			return;
		}
		
		pipelines[pipeline.name] = pipeline;
		
		for (NSString *alias in pipeline.previousNames)
		{
			if (pipelineNameAlias[alias] == nil)
			{
				pipelineNameAlias[alias] = pipeline.name;
			}
		}
		
		if (suspendCount > 0) {
			[pipeline suspendWithCount:suspendCount];
		}
		
	#pragma clang diagnostic pop
	}};
	
	if (dispatch_get_specific(IsOnQueueKey))
		block();
	else
		dispatch_sync(queue, block);
	
	return result;
}

/**
 * Returns all the registered pipelines.
**/
- (NSArray<YapDatabaseCloudCorePipeline *> *)registeredPipelines
{
	__block NSArray *allPipelines = nil;
	
	dispatch_block_t block = ^{ @autoreleasepool {
	#pragma clang diagnostic push
	#pragma clang diagnostic ignored "-Wimplicit-retain-self"
		
		allPipelines = [pipelines allValues];
		
	#pragma clang diagnostic pop
	}};
	
	if (dispatch_get_specific(IsOnQueueKey))
		block();
	else
		dispatch_sync(queue, block);
	
	return allPipelines;
}

/**
 * Returns all the registered pipeline names.
**/
- (NSArray<NSString *> *)registeredPipelineNames
{
	__block NSArray *allPipelineNames = nil;
	
	dispatch_block_t block = ^{ @autoreleasepool {
	#pragma clang diagnostic push
	#pragma clang diagnostic ignored "-Wimplicit-retain-self"
		
		allPipelineNames = [pipelines allKeys];
		
	#pragma clang diagnostic pop
	}};
	
	if (dispatch_get_specific(IsOnQueueKey))
		block();
	else
		dispatch_sync(queue, block);
	
	return allPipelineNames;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark Suspend & Resume
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

- (BOOL)isSuspended
{
	return (self.suspendCount != 0);
}

- (NSUInteger)suspendCount
{
	__block NSUInteger result = 0;
	
	dispatch_block_t block = ^{
	#pragma clang diagnostic push
	#pragma clang diagnostic ignored "-Wimplicit-retain-self"
		
		result = suspendCount;
		
	#pragma clang diagnostic pop
	};
	
	if (dispatch_get_specific(IsOnQueueKey))
		block();
	else
		dispatch_sync(queue, block);
	
	return result;
}

- (NSUInteger)suspend
{
	return [self suspendWithCount:1];
}

- (NSUInteger)suspendWithCount:(NSUInteger)suspendCountIncrement
{
	__block NSUInteger newSuspendCount = 0;
	
	dispatch_block_t block = ^{ @autoreleasepool {
	#pragma clang diagnostic push
	#pragma clang diagnostic ignored "-Wimplicit-retain-self"
		
		if (suspendCount <= (NSUIntegerMax - suspendCountIncrement))
			suspendCount += suspendCountIncrement;
		else {
			suspendCount = NSUIntegerMax;
		}
		
		newSuspendCount = suspendCount;
		
		for (YapDatabaseCloudCorePipeline *pipeline in [pipelines objectEnumerator])
		{
			[pipeline suspendWithCount:suspendCountIncrement];
		}
		
	#pragma clang diagnostic pop
	}};
	
	if (dispatch_get_specific(IsOnQueueKey))
		block();
	else
		dispatch_sync(queue, block);
	
	return newSuspendCount;
}

- (NSUInteger)resume
{
	__block NSUInteger newSuspendCount = 0;
	
	dispatch_block_t block = ^{ @autoreleasepool {
	#pragma clang diagnostic push
	#pragma clang diagnostic ignored "-Wimplicit-retain-self"
		
		if (suspendCount > 0) {
			suspendCount--;
		}
		
		newSuspendCount = suspendCount;
		
		for (YapDatabaseCloudCorePipeline *pipeline in [pipelines objectEnumerator])
		{
			[pipeline resume];
		}
		
	#pragma clang diagnostic pop
	}};
	
	if (dispatch_get_specific(IsOnQueueKey))
		block();
	else
		dispatch_sync(queue, block);
	
	return newSuspendCount;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark Restore & Commit
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Called after the operations have been committed to disk.
**/
- (void)commitAddedGraphs:(NSDictionary<NSString *, YapDatabaseCloudCoreGraph *> *)addedGraphs
       insertedOperations:(NSDictionary<NSString *, NSDictionary *> *)insertedOperations
       modifiedOperations:(NSDictionary<NSUUID *, YapDatabaseCloudCoreOperation *> *)modifiedOperations
{
	YDBLogAutoTrace();
	
	if (addedGraphs.count == 0 &&
	    insertedOperations.count == 0 &&
	    modifiedOperations.count == 0)
	{
		return;
	}
	
	dispatch_block_t block = ^{ @autoreleasepool {
	#pragma clang diagnostic push
	#pragma clang diagnostic ignored "-Wimplicit-retain-self"
		
		for (YapDatabaseCloudCorePipeline *pipeline in [pipelines objectEnumerator])
		{
			YapDatabaseCloudCoreGraph *graphForPipeline = addedGraphs[pipeline.name];
			NSDictionary *insertedForPipeline = insertedOperations[pipeline.name];
			
			[pipeline processAddedGraph:graphForPipeline
			         insertedOperations:insertedForPipeline
			         modifiedOperations:modifiedOperations];
		}
		
	#pragma clang diagnostic pop
	}};
	
	if (dispatch_get_specific(IsOnQueueKey))
		block();
	else
		dispatch_sync(queue, block);
}

@end
