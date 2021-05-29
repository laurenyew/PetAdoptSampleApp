package laurenyew.petadoptsampleapp.di

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import laurenyew.petadoptsampleapp.database.DatabaseManager
import laurenyew.petadoptsampleapp.database.PetAdoptDatabase
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimalDatabaseProvider
import laurenyew.petadoptsampleapp.database.search.SearchAnimalListDatabaseProvider
import laurenyew.petadoptsampleapp.database.search.SearchTermDatabaseProvider
import laurenyew.petadoptsampleapp.repository.PetFavoriteRepository
import laurenyew.petadoptsampleapp.repository.PetSearchRepository
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
    fun provideSearchAnimalListDatabaseProvider(databaseManager: DatabaseManager): SearchAnimalListDatabaseProvider =
        databaseManager


    @Singleton
    @Provides
    fun provideSearchTermDatabaseProvider(databaseManager: DatabaseManager): SearchTermDatabaseProvider =
        databaseManager


    @Singleton
    @Provides
    fun providePetSearchRepository(
        searchPetsCommands: SearchPetsCommands,
        searchAnimalListDatabaseProvider: SearchAnimalListDatabaseProvider,
        searchTermDatabaseProvider: SearchTermDatabaseProvider
    ): PetSearchRepository =
        PetSearchRepository(searchPetsCommands, searchAnimalListDatabaseProvider, searchTermDatabaseProvider)

    @Singleton
    @Provides
    fun providePetFavoriteRepository(favoriteAnimalDatabaseProvider: FavoriteAnimalDatabaseProvider): PetFavoriteRepository =
        PetFavoriteRepository(favoriteAnimalDatabaseProvider)
}