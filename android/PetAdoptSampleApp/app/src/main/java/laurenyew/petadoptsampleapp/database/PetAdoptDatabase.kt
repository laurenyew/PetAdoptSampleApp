package laurenyew.petadoptsampleapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimal
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimalDao
import laurenyew.petadoptsampleapp.database.search.SearchAnimalList
import laurenyew.petadoptsampleapp.database.search.SearchAnimalListDao
import laurenyew.petadoptsampleapp.database.search.SearchTerm
import laurenyew.petadoptsampleapp.database.search.SearchTermDao
import laurenyew.petadoptsampleapp.database.util.Converters

@Database(
    entities = [
        FavoriteAnimal::class,
        SearchAnimalList::class,
        SearchTerm::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class PetAdoptDatabase : RoomDatabase() {
    abstract fun favoriteAnimalDao(): FavoriteAnimalDao
    abstract fun searchAnimalListDao(): SearchAnimalListDao
    abstract fun searchTermDao(): SearchTermDao
}