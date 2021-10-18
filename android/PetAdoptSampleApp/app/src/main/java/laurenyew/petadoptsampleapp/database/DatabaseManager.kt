package laurenyew.petadoptsampleapp.database

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import laurenyew.petadoptsampleapp.database.animal.Animal
import laurenyew.petadoptsampleapp.database.animal.AnimalDatabaseProvider
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimal
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimalDatabaseProvider
import laurenyew.petadoptsampleapp.database.favorite.FavoritesFilterDatabaseProvider
import laurenyew.petadoptsampleapp.database.organization.Organization
import laurenyew.petadoptsampleapp.database.organization.OrganizationDatabaseProvider
import laurenyew.petadoptsampleapp.database.search.SearchAnimalList
import laurenyew.petadoptsampleapp.database.search.SearchTerm
import laurenyew.petadoptsampleapp.database.search.SearchTermDatabaseProvider
import laurenyew.petadoptsampleapp.ui.features.favorites.FavoritesFilter
import javax.inject.Inject

class DatabaseManager @Inject constructor(
    private val database: PetAdoptDatabase,
    private val dataStore: DataStore<Preferences>
) : FavoriteAnimalDatabaseProvider,
    AnimalDatabaseProvider,
    SearchTermDatabaseProvider,
    FavoritesFilterDatabaseProvider,
    OrganizationDatabaseProvider {

    //region Favorite Animal Database
    override suspend fun getAllFavoriteAnimals(): List<FavoriteAnimal> =
        database.favoriteAnimalDao().getFavoriteAnimals()

    override suspend fun getFavoriteAnimal(id: String): FavoriteAnimal? =
        database.favoriteAnimalDao().getFavoriteAnimal(id)

    override suspend fun isAnimalFavorited(id: String): Boolean =
        getFavoriteAnimal(id) != null

    override suspend fun favoriteAnimal(animal: FavoriteAnimal) {
        database.favoriteAnimalDao().insert(animal)
    }

    override suspend fun updateFavoritedAnimal(animal: FavoriteAnimal) {
        database.favoriteAnimalDao().insert(animal)
    }

    override suspend fun unFavoriteAnimal(id: String) {
        database.favoriteAnimalDao().deleteFavoriteAnimal(id)
    }
    //endregion

    //region Search Animal List
    override suspend fun lastUpdatedSearchedAnimalList(): Long? =
        dataStore.data.map {
            it[lastSearchedAnimalListUpdatedTimeKey]
        }.firstOrNull()

    override suspend fun getSearchedAnimalList(searchId: String): List<Animal>? =
        database.searchAnimalListDao().getSearchedAnimalList(searchId)?.animalList

    override suspend fun deleteSearchedAnimalList(searchId: String) {
        database.searchAnimalListDao().deleteSearchedAnimalList(searchId)
        onUpdatedSearchedAnimalList()
    }

    override suspend fun deleteAllSearchedAnimalLists() {
        database.searchAnimalListDao().deleteAllSearchedAnimalLists()
        onUpdatedSearchedAnimalList()
    }

    override suspend fun insertSearchedAnimalList(searchId: String, list: List<Animal>) {
        database.searchAnimalListDao()
            .insert(SearchAnimalList(searchId = searchId, animalList = list))
        onUpdatedSearchedAnimalList()
    }

    /**
     * Update the saved last updated time to our searched animal list
     */
    private suspend fun onUpdatedSearchedAnimalList() {
        dataStore.edit {
            it[lastSearchedAnimalListUpdatedTimeKey] = System.currentTimeMillis()
        }
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
    override suspend fun getFavoritesFilter(): FavoritesFilter? {
        val filters = database.favoritesFilterDao().getFavoriteFilters()
        return if (filters.isNotEmpty()) {
            filters[0]
        } else {
            null
        }
    }

    override suspend fun updateFavoritesFilter(favoritesFilter: FavoritesFilter) {
        database.favoritesFilterDao().insert(favoritesFilter)
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