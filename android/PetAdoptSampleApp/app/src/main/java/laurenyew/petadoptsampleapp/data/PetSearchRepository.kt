package laurenyew.petadoptsampleapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import laurenyew.petadoptsampleapp.data.networking.commands.PetDetailCommands
import laurenyew.petadoptsampleapp.data.networking.commands.SearchPetsCommands
import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.db.search.SearchTerm
import laurenyew.petadoptsampleapp.db.search.SearchTermDatabaseProvider
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class PetSearchRepository @Inject constructor(
    private val searchPetCommand: SearchPetsCommands,
    private val petDetailCommands: PetDetailCommands,
    private val searchTermDatabaseProvider: SearchTermDatabaseProvider
) {
    fun getNearbyPets(query: String): Flow<PagingData<Animal>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PetSearchPagingSource(searchPetCommand, query) }
        ).flow
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
        val searchTerm = SearchTerm(
            searchId = zipcode,
            zipcode = zipcode,
            timestamp = System.currentTimeMillis()
        )
        searchTermDatabaseProvider.insert(searchTerm)
        return zipcode
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}