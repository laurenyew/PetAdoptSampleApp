package laurenyew.petadoptsampleapp.data.models

import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.data.networking.api.responses.Pagination

sealed class PetListResult {
    data class Success(val animals: List<Animal>, val page: Pagination) : PetListResult()
    data class Error(val exception: Exception) : PetListResult()
}
