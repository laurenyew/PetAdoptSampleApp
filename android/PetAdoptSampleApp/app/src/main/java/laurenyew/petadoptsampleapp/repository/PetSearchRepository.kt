package laurenyew.petadoptsampleapp.repository

import laurenyew.petadoptsampleapp.database.animal.AnimalDatabaseProvider
import laurenyew.petadoptsampleapp.database.search.SearchTerm
import laurenyew.petadoptsampleapp.database.search.SearchTermDatabaseProvider
import laurenyew.petadoptsampleapp.database.animal.Animal
import laurenyew.petadoptsampleapp.repository.networking.commands.SearchPetsCommands
import laurenyew.petadoptsampleapp.repository.responses.SearchPetsRepoResponse
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

//TODO: Filters on pet search --> location, type, gender, etc. Save in Room DB (filter name?)
@Singleton
class PetSearchRepository @Inject constructor(
    private val searchPetCommand: SearchPetsCommands,
    private val animalDatabaseProvider: AnimalDatabaseProvider,
    private val searchTermDatabaseProvider: SearchTermDatabaseProvider
) {
    suspend fun getNearbyDogs(zipcode: String): SearchPetsRepoResponse = try {
        val animals = searchPetCommand.searchForNearbyDogs(zipcode)
        val searchId = saveSearchTerm(zipcode)
        saveSearchedAnimalList(searchId, animals)
        SearchPetsRepoResponse.Success(animals)
    } catch (e: Exception) {
        Timber.e(e)
        SearchPetsRepoResponse.Error.Unknown(e)
    }

    suspend fun getSearchTerms(): List<SearchTerm> =
        searchTermDatabaseProvider.getAllSearchTerms()

    suspend fun getLastSearchTerm(): SearchTerm? =
        searchTermDatabaseProvider.getLastSearchTerm()

    /**
     * Save search term into database
     * For now, we treat same zipcode search terms as the same search term
     * @return searchId
     */
    suspend fun saveSearchTerm(zipcode: String): String {
        val searchId = zipcode
        val searchTerm = SearchTerm(
            searchId = searchId,
            zipcode = zipcode,
            timestamp = System.currentTimeMillis()
        )
        searchTermDatabaseProvider.insert(searchTerm)
        return searchId
    }

    suspend fun getSearchedAnimalList(searchId: String): List<Animal>? =
        animalDatabaseProvider.getSearchedAnimalList(searchId)

    private suspend fun saveSearchedAnimalList(searchId: String, animalList: List<Animal>) {
        animalDatabaseProvider.insertSearchedAnimalList(searchId, animalList)
    }

    companion object {
        private const val LAST_SEARCH_ZIPCODE_KEY = "last_search_zipcode_key"
    }
}