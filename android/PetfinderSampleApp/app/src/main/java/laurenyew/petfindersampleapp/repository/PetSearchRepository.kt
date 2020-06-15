package laurenyew.petfindersampleapp.repository

import laurenyew.petfindersampleapp.repository.models.AnimalModel
import laurenyew.petfindersampleapp.repository.networking.commands.SearchPetsCommands
import javax.inject.Inject
import javax.inject.Singleton

//TODO: Filters on pet search --> location, type, gender, etc. Save in Room DB (filter name?)
@Singleton
class PetSearchRepository @Inject constructor(
    private val searchPetCommand: SearchPetsCommands
) {
    suspend fun getNearbyDogs(location: String): List<AnimalModel>? =
        searchPetCommand.searchForNearbyDogs(location)
}