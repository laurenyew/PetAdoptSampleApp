package laurenyew.petfindersampleapp.repository.networking.commands

import android.util.Log
import kotlinx.coroutines.async
import laurenyew.petfindersampleapp.repository.models.AnimalModel
import laurenyew.petfindersampleapp.repository.networking.api.PetfinderApi
import laurenyew.petfindersampleapp.repository.networking.api.responses.SearchPetsNetworkResponse
import retrofit2.Response
import javax.inject.Inject

class SearchPetsCommands @Inject constructor(private val api: PetfinderApi) : BaseNetworkCommand() {

    suspend fun searchForNearbyDogs(location: String): List<AnimalModel>? {
        val deferred = scope.async {
            Log.d(
                TAG, "Executing $SEARCH_FOR_NEARBY_DOGS_TAG"
            )
            val call = api.searchPets(location = location)
            try {
                val response = call.execute()
                parseResponse(response)
            } catch (e: Exception) {
                null
            } finally {
                //Clean up network call and cancel
                call.cancel()
            }
        }
        return deferred.await()
    }

    /**
     * Parse the response from the network call
     */
    @Throws(RuntimeException::class)
    private fun parseResponse(
        networkResponse: Response<SearchPetsNetworkResponse?>?
    ): ArrayList<AnimalModel> {
        val data = networkResponse?.body()
        if (networkResponse?.code() != 200 || data == null) {
            throw RuntimeException(
                "API call failed. Response error: ${networkResponse?.errorBody()?.toString()}"
            )
        } else {
            val animalList = ArrayList<AnimalModel>()
            data.animals.forEach {
                val photo = if (it.photos.isNotEmpty()) {
                    it.photos[0].fullUrl
                } else {
                    null
                }
                animalList.add(AnimalModel(it.id, it.name, photo))
            }
            Log.d(TAG, "Completed command with animal list: ${animalList.size}")
            return animalList
        }
    }

    companion object {
        const val SEARCH_FOR_NEARBY_DOGS_TAG: String = "search_for_nearby_dogs"
        val TAG: String = SearchPetsCommands::class.java.simpleName
    }
}