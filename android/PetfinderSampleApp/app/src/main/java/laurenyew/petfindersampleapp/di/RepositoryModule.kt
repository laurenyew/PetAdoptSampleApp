package laurenyew.petfindersampleapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import laurenyew.petfindersampleapp.database.DatabaseManager
import laurenyew.petfindersampleapp.database.PetfinderDatabase
import laurenyew.petfindersampleapp.database.favorite.FavoriteAnimalDatabaseProvider
import laurenyew.petfindersampleapp.repository.PetFavoriteRepository
import laurenyew.petfindersampleapp.repository.PetSearchRepository
import laurenyew.petfindersampleapp.repository.networking.commands.SearchPetsCommands
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun providePetfinderDatabase(application: Application): PetfinderDatabase =
        Room.databaseBuilder(
            application.applicationContext,
            PetfinderDatabase::class.java,
            "petfinder-database"
        )
            .build()

    @Singleton
    @Provides
    fun provideDatabaseManager(database: PetfinderDatabase): DatabaseManager =
        DatabaseManager(database)

    @Singleton
    @Provides
    fun provideFavoriteAnimalDatabaseProvider(databaseManager: DatabaseManager): FavoriteAnimalDatabaseProvider =
        databaseManager


    @Singleton
    @Provides
    fun providePetSearchRepository(searchPetsCommands: SearchPetsCommands): PetSearchRepository =
        PetSearchRepository(searchPetsCommands)

    @Singleton
    @Provides
    fun providePetFavoriteRepository(favoriteAnimalDatabaseProvider: FavoriteAnimalDatabaseProvider): PetFavoriteRepository =
        PetFavoriteRepository(favoriteAnimalDatabaseProvider)
}