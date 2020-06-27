package laurenyew.petfindersampleapp.repository.responses

import laurenyew.petfindersampleapp.repository.models.AnimalModel

sealed class SearchPetsRepoResponse{
    data class Success(val animals: List<AnimalModel>?): SearchPetsRepoResponse()
    data class Error(val error: Throwable?): SearchPetsRepoResponse()
}