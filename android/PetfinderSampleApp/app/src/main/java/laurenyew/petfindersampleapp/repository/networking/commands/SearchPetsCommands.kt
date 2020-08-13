package laurenyew.petfindersampleapp.repository.networking.commands

import android.util.Log
import kotlinx.coroutines.async
import laurenyew.petfindersampleapp.repository.models.AnimalModel
import laurenyew.petfindersampleapp.repository.models.ContactModel
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

                val description = parseDescription(it.description)
                animalList.add(
                    AnimalModel(
                        id = it.id,
                        orgId = it.organizationId,
                        type = it.type,
                        name = it.name,
                        age = it.age,
                        sex = it.gender,
                        size = it.size,
                        description = description,
                        status = it.status,
                        breed = it.breeds.primary,
                        photoUrl = photo,
                        distance = it.distance,
                        contact = ContactModel(
                            email = it.contact.email,
                            phone = it.contact.phone,
                            address = it.contact.address.address1 + "\n"
                                    + it.contact.address.city + ", "
                                    + it.contact.address.state + ", "
                                    + it.contact.address.postcode
                        )
                    )
                )
            }
            Log.d(TAG, "Completed command with animal list: ${animalList.size}")
            return animalList
        }
    }

    private fun parseDescription(description: String?): String? = description?.apply {
        replace("&#039;", "'")
    }

    companion object {
        const val SEARCH_FOR_NEARBY_DOGS_TAG: String = "search_for_nearby_dogs"
        val TAG: String = SearchPetsCommands::class.java.simpleName
    }
}