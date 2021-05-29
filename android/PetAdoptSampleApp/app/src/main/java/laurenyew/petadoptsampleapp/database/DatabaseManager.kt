package laurenyew.petadoptsampleapp.database

import laurenyew.petadoptsampleapp.database.animal.Animal
import laurenyew.petadoptsampleapp.database.animal.AnimalDatabaseProvider
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimal
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimalDatabaseProvider
import laurenyew.petadoptsampleapp.database.search.SearchAnimalList
import laurenyew.petadoptsampleapp.database.search.SearchTerm
import laurenyew.petadoptsampleapp.database.search.SearchTermDatabaseProvider
import javax.inject.Inject

class DatabaseManager @Inject constructor(
    private val database: PetAdoptDatabase
) : FavoriteAnimalDatabaseProvider,
    AnimalDatabaseProvider,
    SearchTermDatabaseProvider {
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

    override suspend fun unFavoriteAnimal(id: String) {
        database.favoriteAnimalDao().deleteFavoriteAnimal(id)
    }
    //endregion

    //region Search Animal List
    override suspend fun getSearchedAnimalList(searchId: String): List<Animal>? =
        database.searchAnimalListDao().getSearchedAnimalList(searchId)?.animalList

    override suspend fun deleteSearchedAnimalList(searchId: String) {
        database.searchAnimalListDao().deleteSearchedAnimalList(searchId)
    }

    override suspend fun deleteAllSearchedAnimalLists() {
        database.searchAnimalListDao().deleteAllSearchedAnimalLists()
    }

    override suspend fun insertSearchedAnimalList(searchId: String, list: List<Animal>) {
        database.searchAnimalListDao()
            .insert(SearchAnimalList(searchId = searchId, animalList = list))
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
    //endregion
}