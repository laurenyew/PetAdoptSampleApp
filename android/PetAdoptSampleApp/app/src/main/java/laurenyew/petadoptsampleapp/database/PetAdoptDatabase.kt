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
import laurenyew.petadoptsampleapp.ui.features.favorites.FavoriteFilterDao
import laurenyew.petadoptsampleapp.ui.features.favorites.FavoritesFilter

@Database(
    entities = [
        FavoriteAnimal::class,
        SearchAnimalList::class,
        SearchTerm::class,
        FavoritesFilter::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class PetAdoptDatabase : RoomDatabase() {
    abstract fun favoriteAnimalDao(): FavoriteAnimalDao
    abstract fun searchAnimalListDao(): SearchAnimalListDao
    abstract fun searchTermDao(): SearchTermDao
    abstract fun favoritesFilterDao(): FavoriteFilterDao
}