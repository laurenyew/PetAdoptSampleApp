package laurenyew.petadoptsampleapp.repository.networking.commands

import laurenyew.petadoptsampleapp.repository.models.AnimalModel
import laurenyew.petadoptsampleapp.repository.models.ContactModel
import laurenyew.petadoptsampleapp.repository.networking.api.PetFinderApi
import laurenyew.petadoptsampleapp.repository.networking.api.responses.SearchPetsNetworkResponse
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class SearchPetsCommands @Inject constructor(
    private val api: PetFinderApi
) {
    suspend fun searchForNearbyDogs(location: String): List<AnimalModel> {
        Timber.d("Executing $SEARCH_FOR_NEARBY_DOGS_TAG")
        val response = api.searchPets(location = location)
        return parseResponse(response)
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
            val error = "API call failed. Response error: ${networkResponse?.errorBody()?.string()}"
            Timber.e(error)
            throw RuntimeException(error)
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
            Timber.d("Completed command with animal list: ${animalList.size}")
            return animalList
        }
    }

    private fun parseDescription(description: String?): String? = description?.apply {
        replace("&#039;", "'")
    }

    companion object {
        const val SEARCH_FOR_NEARBY_DOGS_TAG: String = "search_for_nearby_dogs"
    }
}