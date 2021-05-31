package laurenyew.petadoptsampleapp.repository.networking.api.responses

import com.squareup.moshi.Json

//TODO: Use Jetpack Compose Pages
data class SearchPetsNetworkResponse(
    @Json(name = "animals") val animals: List<AnimalResponse>,
    @Json(name = "pagination") val pagination: Pagination
)