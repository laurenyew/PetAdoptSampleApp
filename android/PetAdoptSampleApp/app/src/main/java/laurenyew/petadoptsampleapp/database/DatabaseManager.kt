package laurenyew.petadoptsampleapp.database

import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimal
import laurenyew.petadoptsampleapp.database.favorite.FavoriteAnimalDatabaseProvider
import laurenyew.petadoptsampleapp.database.search.SearchAnimalList
import laurenyew.petadoptsampleapp.database.search.SearchAnimalListDatabaseProvider
import laurenyew.petadoptsampleapp.database.search.SearchTerm
import laurenyew.petadoptsampleapp.database.search.SearchTermDatabaseProvider
import laurenyew.petadoptsampleapp.repository.models.AnimalModel
import javax.inject.Inject

class DatabaseManager @Inject constructor(
    private val database: PetAdoptDatabase
) : FavoriteAnimalDatabaseProvider,
    SearchAnimalListDatabaseProvider,
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
    override suspend fun getSearchedAnimalList(searchId: String): List<AnimalModel>? =
        database.searchAnimalListDao().getSearchedAnimalList(searchId)?.animalList

    override suspend fun deleteSearchedAnimalList(searchId: String) {
        database.searchAnimalListDao().deleteSearchedAnimalList(searchId)
    }

    override suspend fun insertSearchedAnimalList(searchId: String, list: List<AnimalModel>) {
        database.searchAnimalListDao()
            .insert(SearchAnimalList(searchId = searchId, animalList = list))
    }
    //endregion

    //region Search Terms
    override suspend fun getAllSearchTerms(): List<SearchTerm> =
        database.searchTermDao().getAllSearchTerms()

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