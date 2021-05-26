package laurenyew.petadoptsampleapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimal
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimalDao

@Database(
    entities = [FavoriteAnimal::class],
    version = 1
)
abstract class PetAdoptDatabase : RoomDatabase() {
    abstract fun favoriteAnimalDao(): FavoriteAnimalDao
}