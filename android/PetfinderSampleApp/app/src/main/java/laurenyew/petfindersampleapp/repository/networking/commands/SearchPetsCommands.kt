package laurenyew.petfindersampleapp.repository.networking.commands

import android.util.Log
import kotlinx.coroutines.async
import laurenyew.petfindersampleapp.repository.models.AnimalModel
import laurenyew.petfindersampleapp.repository.networking.api.responses.SearchPetsResponse
import retrofit2.Response

class SearchPetsCommands : BaseNetworkCommand() {
    @Throws(RuntimeException::class)
    suspend fun searchForNearbyDogs(location: String): List<AnimalModel>? {
        val deferred = async {
            Log.d(
                TAG, "Executing $SEARCH_FOR_NEARBY_DOGS_TAG"
            )
            val call = api?.searchPets(location = location)
            try {
                val response = call?.execute()
                parseResponse(response)
            } finally {
                //Clean up network call and cancel
                call?.cancel()
            }
        }
        return deferred.await()
    }

    /**
     * Parse the response from the network call
     */
    @Throws(RuntimeException::class)
    private fun parseResponse(
        response: Response<SearchPetsResponse?>?
    ): ArrayList<AnimalModel> {
        val data = response?.body()
        if (response?.code() != 200 || data == null) {
            throw RuntimeException("API call failed. Response error: ${response?.errorBody()?.toString()}")
        } else {
            val animalList = ArrayList<AnimalModel>()
            data.animals.forEach {
                animalList.add(AnimalModel(it.id, it.name, it.photos[0].full))
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