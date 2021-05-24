package laurenyew.petfindersampleapp.repository

import laurenyew.petfindersampleapp.repository.networking.commands.SearchPetsCommands
import laurenyew.petfindersampleapp.repository.responses.SearchPetsRepoResponse
import javax.inject.Inject
import javax.inject.Singleton

//TODO: Filters on pet search --> location, type, gender, etc. Save in Room DB (filter name?)
@Singleton
class PetSearchRepository @Inject constructor(
    private val searchPetCommand: SearchPetsCommands
) {
    suspend fun getNearbyDogs(location: String): SearchPetsRepoResponse =
        try {
            val animals = searchPetCommand.searchForNearbyDogs(location)
            SearchPetsRepoResponse.Success(animals)
        } catch (e: Exception) {
            SearchPetsRepoResponse.Error(e)
        }
}