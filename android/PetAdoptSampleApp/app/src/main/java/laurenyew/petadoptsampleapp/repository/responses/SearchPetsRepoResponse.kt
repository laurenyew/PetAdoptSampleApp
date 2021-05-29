package laurenyew.petadoptsampleapp.repository.responses

import laurenyew.petadoptsampleapp.repository.models.AnimalModel

sealed class SearchPetsRepoResponse {
    data class Success(val animals: List<AnimalModel>?) : SearchPetsRepoResponse()

    sealed class Error : SearchPetsRepoResponse() {
        object NoSearchTermProvided : Error()
        data class Unknown(val error: Throwable?) : Error()
    }
}