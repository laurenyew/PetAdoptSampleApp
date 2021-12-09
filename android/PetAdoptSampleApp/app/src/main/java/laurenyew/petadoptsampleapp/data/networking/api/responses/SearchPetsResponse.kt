package laurenyew.petadoptsampleapp.data.networking.api.responses

import com.squareup.moshi.Json

data class SearchPetsNetworkResponse(
    @Json(name = "animals") val animals: List<AnimalResponse>,
    @Json(name = "pagination") val pagination: Pagination
)