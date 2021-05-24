package laurenyew.petfindersampleapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import laurenyew.petfindersampleapp.database.favorite.FavoriteAnimal
import laurenyew.petfindersampleapp.database.favorite.FavoriteAnimalDao

@Database(
    entities = [FavoriteAnimal::class],
    version = 1
)
abstract class PetfinderDatabase : RoomDatabase() {
    abstract fun favoriteAnimalDao(): FavoriteAnimalDao
}