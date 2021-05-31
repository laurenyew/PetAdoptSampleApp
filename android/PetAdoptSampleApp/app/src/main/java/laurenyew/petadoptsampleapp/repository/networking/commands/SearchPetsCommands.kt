package laurenyew.petadoptsampleapp.repository.networking.commands

import laurenyew.petadoptsampleapp.database.animal.Animal
import laurenyew.petadoptsampleapp.repository.networking.api.PetFinderApi
import laurenyew.petadoptsampleapp.repository.networking.api.responses.SearchPetsNetworkResponse
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class SearchPetsCommands @Inject constructor(
    private val api: PetFinderApi
) {
    suspend fun searchForNearbyPets(location: String): List<Animal> {
        Timber.d("Executing Search for Nearby Pets: $location")
        val response = api.searchPets(location = location)
        return parseResponse(response)
    }

    /**
     * Parse the response from the network call
     */
    @Throws(RuntimeException::class)
    private fun parseResponse(
        networkResponse: Response<SearchPetsNetworkResponse?>?
    ): List<Animal> {
        val data = networkResponse?.body()
        if (networkResponse?.code() != 200 || data == null) {
            val error = "API call failed. Response error: ${networkResponse?.errorBody()?.string()}"
            Timber.e(error)
            throw RuntimeException(error)
        } else {
            val animalList = ArrayList<Animal>()
            data.animals.forEach {
                animalList.add(
                    it.toAnimal()
                )
            }
            Timber.d("Completed command with animal list: ${animalList.size}")
            return animalList
        }
    }
}