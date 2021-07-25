package laurenyew.petadoptsampleapp.di

import android.app.Application
import android.content.SharedPreferences
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
import laurenyew.petadoptsampleapp.database.favorite.FavoritesFilterDatabaseProvider
import laurenyew.petadoptsampleapp.database.organization.OrganizationDatabaseProvider
import laurenyew.petadoptsampleapp.database.search.SearchTermDatabaseProvider
import laurenyew.petadoptsampleapp.repository.OrganizationSearchRepository
import laurenyew.petadoptsampleapp.repository.PetFavoriteRepository
import laurenyew.petadoptsampleapp.repository.PetSearchRepository
import laurenyew.petadoptsampleapp.repository.networking.commands.PetDetailCommands
import laurenyew.petadoptsampleapp.repository.networking.commands.SearchOrganizationsCommands
import laurenyew.petadoptsampleapp.repository.networking.commands.SearchPetsCommands
import laurenyew.petadoptsampleapp.repository.poll.PollManager
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
    fun provideFavoritesFilterDatabaseProvider(databaseManager: DatabaseManager): FavoritesFilterDatabaseProvider =
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
        favoritesFilterDatabaseProvider: FavoritesFilterDatabaseProvider,
        pollManager: PollManager,
        externalScope: CoroutineScope,
    ): PetFavoriteRepository =
        PetFavoriteRepository(
            petDetailCommands,
            favoriteAnimalDatabaseProvider,
            favoritesFilterDatabaseProvider,
            pollManager,
            externalScope
        )

    @Singleton
    @Provides
    fun providePollManager(
        sharedPreferences: SharedPreferences,
        applicationScope: CoroutineScope
    ): PollManager =
        PollManager(sharedPreferences, applicationScope)


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