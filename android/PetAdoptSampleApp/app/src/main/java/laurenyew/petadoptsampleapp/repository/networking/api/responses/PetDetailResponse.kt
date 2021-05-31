package laurenyew.petadoptsampleapp.repository.networking.api.responses

import com.squareup.moshi.Json

data class PetDetailResponse(
    @Json(name = "animal") val animal: AnimalResponse
)