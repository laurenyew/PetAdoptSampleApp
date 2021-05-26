package laurenyew.petadoptsampleapp.repository.responses

import laurenyew.petadoptsampleapp.repository.models.AnimalModel

sealed class SearchPetsRepoResponse{
    data class Success(val animals: List<AnimalModel>?): SearchPetsRepoResponse()
    data class Error(val error: Throwable?): SearchPetsRepoResponse()
}