package laurenyew.petadoptsampleapp.repository.responses

import laurenyew.petadoptsampleapp.database.animal.Animal

sealed class SearchPetsRepoResponse {
    data class Success(val animals: List<Animal>?) : SearchPetsRepoResponse()

    sealed class Error : SearchPetsRepoResponse() {
        object NoSearchTermProvided : Error()
        data class Unknown(val error: Throwable?) : Error()
    }
}