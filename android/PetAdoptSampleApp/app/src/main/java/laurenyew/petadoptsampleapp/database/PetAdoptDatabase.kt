package laurenyew.petadoptsampleapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimal
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimalDao
import laurenyew.petadoptsampleapp.database.search.SearchAnimalList
import laurenyew.petadoptsampleapp.database.search.SearchAnimalListDao
import laurenyew.petadoptsampleapp.database.search.SearchTerm
import laurenyew.petadoptsampleapp.database.search.SearchTermDao

@Database(
    entities = [
        FavoriteAnimal::class,
        SearchAnimalList::class,
        SearchTerm::class],
    version = 2
)
abstract class PetAdoptDatabase : RoomDatabase() {
    abstract fun favoriteAnimalDao(): FavoriteAnimalDao
    abstract fun searchAnimalListDao(): SearchAnimalListDao
    abstract fun searchTermDao(): SearchTermDao
}