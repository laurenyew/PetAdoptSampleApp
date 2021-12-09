package laurenyew.petadoptsampleapp.db

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.db.animal.AnimalDatabaseProvider
import laurenyew.petadoptsampleapp.db.favorite.FavoriteAnimalDatabaseProvider
import laurenyew.petadoptsampleapp.db.filter.AnimalFilter
import laurenyew.petadoptsampleapp.db.filter.AnimalFilterDatabaseProvider
import laurenyew.petadoptsampleapp.db.organization.Organization
import laurenyew.petadoptsampleapp.db.organization.OrganizationDatabaseProvider
import laurenyew.petadoptsampleapp.db.search.SearchTerm
import laurenyew.petadoptsampleapp.db.search.SearchTermDatabaseProvider
import javax.inject.Inject

class DatabaseManager @Inject constructor(
    private val database: PetAdoptDatabase,
    private val dataStore: DataStore<Preferences>
) : AnimalDatabaseProvider,
    FavoriteAnimalDatabaseProvider,
    SearchTermDatabaseProvider,
    AnimalFilterDatabaseProvider,
    OrganizationDatabaseProvider {

    //region Saved Animals
    override suspend fun getAnimals(): List<Animal> =
        database.animalListDao().getAnimals()

    override suspend fun getAnimal(id: String): Animal? =
        database.animalListDao().getAnimal(id)

    override suspend fun deleteAnimal(id: String) {
        database.animalListDao().deleteAnimal(id)
    }

    override suspend fun updateAnimal(animal: Animal) {
        database.animalListDao().insert(animal)
    }

    override suspend fun deleteAllAnimals() {
        database.animalListDao().deleteAllAnimals()
    }
    //endregion

    //region Favorite Animal Database
    override suspend fun getAllFavoriteAnimals(): List<Animal> =
        database.favoriteAnimalDao().getFavoriteAnimals()

    override suspend fun isAnimalFavorited(id: String): Boolean =
        database.animalListDao().getAnimal(id)?.isFavorite == true

    override suspend fun favoriteAnimal(animal: Animal) {
        database.animalListDao().insert(animal.copy(true))
    }

    override suspend fun unFavoriteAnimal(animal: Animal) {
        database.animalListDao().insert(animal.copy(false))
    }
    //endregion


    //region Search Terms
    override suspend fun getAllSearchTerms(): List<SearchTerm> =
        database.searchTermDao().getAllSearchTerms()

    override suspend fun getLastSearchTerm(): SearchTerm? {
        val orderedSearchTerms = getAllSearchTerms()
        return if (orderedSearchTerms.isNotEmpty()) {
            orderedSearchTerms[0]
        } else {
            null
        }
    }

    override suspend fun getSearchTerm(searchId: String): SearchTerm? =
        database.searchTermDao().getSearchTerm(searchId)

    override suspend fun deleteSearchTerm(searchId: String) {
        database.searchTermDao().deleteSearchTerm(searchId)
    }

    override suspend fun insert(searchTerm: SearchTerm) {
        database.searchTermDao().insert(searchTerm)
    }

    override suspend fun updateSearchTermTimeStamp(searchId: String) {
        getSearchTerm(searchId)?.let {
            insert(it.copy(timestamp = System.currentTimeMillis()))
        }
    }

    //endregion
    //region Favorites Filter
    override suspend fun animalFilter(): AnimalFilter? {
        val filters = database.animalFilterDao().getAnimalFilters()
        return if (filters.isNotEmpty()) {
            filters[0]
        } else {
            null
        }
    }

    override suspend fun updateAnimalFilter(animalFilter: AnimalFilter) {
        database.animalFilterDao().insert(animalFilter)
    }
    //endregion

    //region Organizations
    override suspend fun getOrganizations(searchId: String): List<Organization> =
        database.organizationDao().getOrganizations(searchId)

    override suspend fun deleteOrganizations() {
        database.organizationDao().deleteAllOrganizations()
    }

    override suspend fun insertOrganizations(organizations: List<Organization>) {
        deleteOrganizations()
        organizations.forEach {
            database.organizationDao().insert(it)
        }
    }
    //endregion

    companion object {
        private val lastSearchedAnimalListUpdatedTimeKey =
            longPreferencesKey("lastSearchedAnimalListUpdatedTime")
    }
}