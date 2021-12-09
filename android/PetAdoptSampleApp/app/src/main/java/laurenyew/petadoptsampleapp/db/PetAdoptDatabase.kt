package laurenyew.petadoptsampleapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.db.animal.AnimalDao
import laurenyew.petadoptsampleapp.db.favorite.FavoriteAnimalDao
import laurenyew.petadoptsampleapp.db.filter.AnimalFilter
import laurenyew.petadoptsampleapp.db.filter.AnimalFilterDao
import laurenyew.petadoptsampleapp.db.organization.Organization
import laurenyew.petadoptsampleapp.db.organization.OrganizationDao
import laurenyew.petadoptsampleapp.db.search.SearchTerm
import laurenyew.petadoptsampleapp.db.search.SearchTermDao
import laurenyew.petadoptsampleapp.db.util.Converters

@Database(
    entities = [
        Animal::class,
        SearchTerm::class,
        AnimalFilter::class,
        Organization::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PetAdoptDatabase : RoomDatabase() {
    abstract fun favoriteAnimalDao(): FavoriteAnimalDao
    abstract fun animalListDao(): AnimalDao
    abstract fun searchTermDao(): SearchTermDao
    abstract fun animalFilterDao(): AnimalFilterDao
    abstract fun organizationDao(): OrganizationDao
}