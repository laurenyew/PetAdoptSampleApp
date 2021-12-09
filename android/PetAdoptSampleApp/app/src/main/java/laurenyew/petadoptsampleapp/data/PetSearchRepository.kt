package laurenyew.petadoptsampleapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.db.animal.AnimalDatabaseProvider
import laurenyew.petadoptsampleapp.db.search.SearchTerm
import laurenyew.petadoptsampleapp.db.search.SearchTermDatabaseProvider
import laurenyew.petadoptsampleapp.data.networking.commands.PetDetailCommands
import laurenyew.petadoptsampleapp.data.networking.commands.SearchPetsCommands
import laurenyew.petadoptsampleapp.data.responses.SearchPetsRepoResponse
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class PetSearchRepository @Inject constructor(
    private val searchPetCommand: SearchPetsCommands,
    private val petDetailCommands: PetDetailCommands,
    private val animalDatabaseProvider: AnimalDatabaseProvider,
    private val searchTermDatabaseProvider: SearchTermDatabaseProvider
) {

    suspend fun getNearbyPets(zipcode: String): SearchPetsRepoResponse = try {
        val animals = searchPetCommand.searchForNearbyPets(zipcode)
        val searchId = saveSearchTerm(zipcode)
        saveSearchedAnimalList(searchId, animals)
        SearchPetsRepoResponse.Success(animals)
    } catch (e: Exception) {
        Timber.e(e)
        SearchPetsRepoResponse.Error.Unknown(e)
    }

    suspend fun getAnimalDetails(animalId: Long): Animal? =
        petDetailCommands.fetchAnimalDetails(animalId)

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

    companion object {
        private val cacheTimeout = TimeUnit.HOURS.convert(1, TimeUnit.MILLISECONDS)
    }
}