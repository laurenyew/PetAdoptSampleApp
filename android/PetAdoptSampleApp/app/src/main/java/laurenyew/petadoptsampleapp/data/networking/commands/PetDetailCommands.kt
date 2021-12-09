package laurenyew.petadoptsampleapp.data.networking.commands

import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.data.networking.api.PetFinderApi
import laurenyew.petadoptsampleapp.data.networking.api.responses.PetDetailResponse
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class PetDetailCommands @Inject constructor(
    private val api: PetFinderApi
) {
    suspend fun fetchAnimalDetails(animalId: Long): Animal? {
        Timber.d("Executing Fetch Animal Details for id: $animalId")
        val response = api.getPetDetail(animalId.toString())
        return parseResponse(response)
    }

    /**
     * Parse the response from the network call
     */
    @Throws(RuntimeException::class)
    private fun parseResponse(
        networkResponse: Response<PetDetailResponse?>?
    ): Animal {
        val data = networkResponse?.body()
        if (networkResponse?.code() != 200 || data == null) {
            val error = "API call failed. Response error: ${networkResponse?.errorBody()?.string()}"
            Timber.e(error)
            throw RuntimeException(error)
        } else {
            val animal = data.animal.toAnimal()
            Timber.d("Completed command with animal: $animal")
            return animal
        }
    }
}