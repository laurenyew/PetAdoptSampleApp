package laurenyew.petadoptsampleapp.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import laurenyew.petadoptsampleapp.data.OrganizationSearchRepository
import laurenyew.petadoptsampleapp.data.PetFavoriteRepository
import laurenyew.petadoptsampleapp.data.PetSearchRepository
import laurenyew.petadoptsampleapp.data.networking.commands.PetDetailCommands
import laurenyew.petadoptsampleapp.data.networking.commands.SearchOrganizationsCommands
import laurenyew.petadoptsampleapp.data.networking.commands.SearchPetsCommands
import laurenyew.petadoptsampleapp.data.poll.PollManager
import laurenyew.petadoptsampleapp.db.DatabaseManager
import laurenyew.petadoptsampleapp.db.PetAdoptDatabase
import laurenyew.petadoptsampleapp.db.animal.AnimalDatabaseProvider
import laurenyew.petadoptsampleapp.db.favorite.FavoriteAnimalDatabaseProvider
import laurenyew.petadoptsampleapp.db.filter.AnimalFilterDatabaseProvider
import laurenyew.petadoptsampleapp.db.organization.OrganizationDatabaseProvider
import laurenyew.petadoptsampleapp.db.search.SearchTermDatabaseProvider
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
    fun provideDatabaseManager(
        database: PetAdoptDatabase,
        dataStore: DataStore<Preferences>,
    ): DatabaseManager =
        DatabaseManager(database, dataStore)

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
    fun provideFavoritesFilterDatabaseProvider(databaseManager: DatabaseManager): AnimalFilterDatabaseProvider =
        databaseManager

    @Singleton
    @Provides
    fun provideOrganizationDatabaseProvider(databaseManager: DatabaseManager): OrganizationDatabaseProvider =
        databaseManager


    @Singleton
    @Provides
    fun providePetSearchRepository(
        searchPetsCommands: SearchPetsCommands,
        petDetailCommands: PetDetailCommands,
        animalDatabaseProvider: AnimalDatabaseProvider,
        searchTermDatabaseProvider: SearchTermDatabaseProvider,
        pollManager: PollManager,
        externalScope: CoroutineScope,
    ): PetSearchRepository =
        PetSearchRepository(
            searchPetsCommands,
            petDetailCommands,
            searchTermDatabaseProvider,
        )

    @Singleton
    @Provides
    fun providePetFavoriteRepository(
        petDetailCommands: PetDetailCommands,
        favoriteAnimalDatabaseProvider: FavoriteAnimalDatabaseProvider,
        animalFilterDatabaseProvider: AnimalFilterDatabaseProvider,
        pollManager: PollManager,
        externalScope: CoroutineScope,
    ): PetFavoriteRepository =
        PetFavoriteRepository(
            petDetailCommands,
            favoriteAnimalDatabaseProvider,
            animalFilterDatabaseProvider,
            pollManager,
            externalScope
        )

    @Singleton
    @Provides
    fun providePollManager(
        dataStore: DataStore<Preferences>,
        applicationScope: CoroutineScope
    ): PollManager =
        PollManager(dataStore, applicationScope)


    @Singleton
    @Provides
    fun providOrganizationSearchRepository(
        searchOrganizationsCommands: SearchOrganizationsCommands,
        organizationDatabaseProvider: OrganizationDatabaseProvider,
        petSearchRepository: PetSearchRepository,
        pollManager: PollManager,
        externalScope: CoroutineScope,
    ): OrganizationSearchRepository =
        OrganizationSearchRepository(
            searchOrganizationsCommands,
            organizationDatabaseProvider,
            petSearchRepository,
            pollManager,
            externalScope
        )
}