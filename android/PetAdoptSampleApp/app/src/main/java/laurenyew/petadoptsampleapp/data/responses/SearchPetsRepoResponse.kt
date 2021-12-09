package laurenyew.petadoptsampleapp.data.responses

import laurenyew.petadoptsampleapp.db.animal.Animal

sealed class SearchPetsRepoResponse {
    data class Success(val animals: List<Animal>?) : SearchPetsRepoResponse()

    sealed class Error : SearchPetsRepoResponse() {
        object NoSearchTermProvided : Error()
        data class Unknown(val error: Throwable?) : Error()
    }
}