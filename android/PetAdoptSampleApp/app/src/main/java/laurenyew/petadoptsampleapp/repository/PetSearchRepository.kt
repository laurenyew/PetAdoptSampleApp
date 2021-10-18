package laurenyew.petadoptsampleapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import laurenyew.petadoptsampleapp.database.animal.Animal
import laurenyew.petadoptsampleapp.database.animal.AnimalDatabaseProvider
import laurenyew.petadoptsampleapp.database.search.SearchTerm
import laurenyew.petadoptsampleapp.database.search.SearchTermDatabaseProvider
import laurenyew.petadoptsampleapp.repository.networking.commands.PetDetailCommands
import laurenyew.petadoptsampleapp.repository.networking.commands.SearchPetsCommands
import laurenyew.petadoptsampleapp.repository.poll.PollManager
import laurenyew.petadoptsampleapp.repository.responses.SearchPetsRepoResponse
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
    private val searchTermDatabaseProvider: SearchTermDatabaseProvider,
    pollManager: PollManager,
    externalScope: CoroutineScope
) : RemoteMediator<Int, Animal>() {
    init {
        // Poll flow when we need to refresh
        pollManager.dataRefreshRequiredFlow
            .map { currentPollTime ->
                getSearchTerms().filter {
                    pollManager.isPastInterval(
                        currentPollTime,
                        it.timestamp
                    )
                }
            }
            .onEach { expiredSearchTerms ->
                // Clear all saved searched animal lists, we need to refresh the data.
                expiredSearchTerms.forEach {
                    val searchId = it.searchId
                    animalDatabaseProvider.deleteSearchedAnimalList(searchId)
                    searchTermDatabaseProvider.updateSearchTermTimeStamp(searchId)
                }
            }.launchIn(externalScope)
    }

    override suspend fun initialize(): InitializeAction {
        val lastUpdated = animalDatabaseProvider.lastUpdatedSearchedAnimalList() ?: 0
        return if (System.currentTimeMillis() - lastUpdated >= cacheTimeout) {
            // Cached data is up-to-date, so there is no need to re-fetch from network.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Need to refresh cached data from network; returning LAUNCH_INITIAL_REFRESH here
            // will also block RemoteMediator's APPEND and PREPEND from running until REFRESH
            // succeeds.
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Animal>): MediatorResult {
        TODO("Not yet implemented")
    }

    suspend fun getNearbyPets(zipcode: String): SearchPetsRepoResponse = try {
        val animals = searchPetCommand.searchForNearbyPets(zipcode)
        val searchId = saveSearchTerm(zipcode)
        saveSearchedAnimalList(searchId, animals)
        SearchPetsRepoResponse.Success(animals)
    } catch (e: Exception) {
        Timber.e(e)
        SearchPetsRepoResponse.Error.Unknown(e)
    }

    suspend fun getAnimalDetails(animalId: String): Animal? =
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

    suspend fun getSearchedAnimalList(searchId: String): List<Animal>? =
        animalDatabaseProvider.getSearchedAnimalList(searchId)

    private suspend fun saveSearchedAnimalList(searchId: String, animalList: List<Animal>) {
        animalDatabaseProvider.insertSearchedAnimalList(searchId, animalList)
    }

    companion object {
        private val cacheTimeout = TimeUnit.HOURS.convert(1, TimeUnit.MILLISECONDS)
    }
}