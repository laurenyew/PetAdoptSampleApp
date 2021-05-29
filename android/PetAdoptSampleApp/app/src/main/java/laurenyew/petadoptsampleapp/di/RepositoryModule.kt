package laurenyew.petadoptsampleapp.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import laurenyew.petadoptsampleapp.database.DatabaseManager
import laurenyew.petadoptsampleapp.database.PetAdoptDatabase
import laurenyew.petadoptsampleapp.database.animal.AnimalDatabaseProvider
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimalDatabaseProvider
import laurenyew.petadoptsampleapp.database.search.SearchTermDatabaseProvider
import laurenyew.petadoptsampleapp.repository.PetFavoriteRepository
import laurenyew.petadoptsampleapp.repository.PetSearchRepository
import laurenyew.petadoptsampleapp.repository.PollManager
import laurenyew.petadoptsampleapp.repository.networking.commands.PetDetailCommands
import laurenyew.petadoptsampleapp.repository.networking.commands.SearchPetsCommands
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun providePetAdoptDatabase(application: Application): PetAdoptDatabase =
        Room.databaseBuilder(
            application.applicationContext,
            PetAdoptDatabase::class.java,
            "petadopt-database"
        )
            .build()

    @Singleton
    @Provides
    fun provideDatabaseManager(database: PetAdoptDatabase): DatabaseManager =
        DatabaseManager(database)

    @Singleton
    @Provides
    fun provideFavoriteAnimalDatabaseProvider(databaseManager: DatabaseManager): FavoriteAnimalDatabaseProvider =
        databaseManager

    @Singleton
    @Provides
    fun provideSearchAnimalListDatabaseProvider(databaseManager: DatabaseManager): AnimalDatabaseProvider =
        databaseManager


    @Singleton
    @Provides
    fun provideSearchTermDatabaseProvider(databaseManager: DatabaseManager): SearchTermDatabaseProvider =
        databaseManager


    @Singleton
    @Provides
    fun providePetSearchRepository(
        searchPetsCommands: SearchPetsCommands,
        animalDatabaseProvider: AnimalDatabaseProvider,
        searchTermDatabaseProvider: SearchTermDatabaseProvider,
        pollManager: PollManager,
        externalScope: CoroutineScope,
    ): PetSearchRepository =
        PetSearchRepository(
            searchPetsCommands,
            animalDatabaseProvider,
            searchTermDatabaseProvider,
            pollManager,
            externalScope
        )

    @Singleton
    @Provides
    fun providePetFavoriteRepository(
        petDetailCommands: PetDetailCommands,
        favoriteAnimalDatabaseProvider: FavoriteAnimalDatabaseProvider,
        pollManager: PollManager,
        externalScope: CoroutineScope,
    ): PetFavoriteRepository =
        PetFavoriteRepository(
            petDetailCommands,
            favoriteAnimalDatabaseProvider,
            pollManager,
            externalScope
        )

    @Singleton
    @Provides
    fun providePollManager(applicationScope: CoroutineScope): PollManager =
        PollManager(applicationScope)
}