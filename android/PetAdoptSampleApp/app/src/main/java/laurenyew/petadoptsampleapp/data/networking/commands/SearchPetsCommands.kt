package laurenyew.petadoptsampleapp.data.networking.commands

import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.data.models.PetListResult
import laurenyew.petadoptsampleapp.data.networking.api.PetFinderApi
import laurenyew.petadoptsampleapp.data.networking.api.responses.SearchPetsNetworkResponse
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class SearchPetsCommands @Inject constructor(
    private val api: PetFinderApi
) {
    suspend fun searchForNearbyPets(location: String, page: Int, numItemsPerPage: Int): PetListResult {
        Timber.d("Executing Search for Nearby Pets: location: $location page: $page")
        val response = api.searchPets(location = location, page = page)
        return parseResponse(response)
    }

    /**
     * Parse the response from the network call
     */
    @Throws(RuntimeException::class)
    private fun parseResponse(
        networkResponse: Response<SearchPetsNetworkResponse?>?
    ): PetListResult {
        val data = networkResponse?.body()
        return if (networkResponse?.code() != 200 || data == null) {
            val error = "API call failed. Response error: ${networkResponse?.errorBody()?.string()}"
            Timber.e(error)
            PetListResult.Error(RuntimeException(error))
        } else {
            val animalList = ArrayList<Animal>()
            data.animals.forEach {
                animalList.add(
                    it.toAnimal()
                )
            }
            Timber.d("Completed command with animal list: ${animalList.size}")
            PetListResult.Success(animalList, data.pagination)
        }
    }
}