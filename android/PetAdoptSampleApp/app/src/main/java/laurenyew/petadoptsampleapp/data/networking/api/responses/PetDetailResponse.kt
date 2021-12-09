package laurenyew.petadoptsampleapp.data.networking.api.responses

import com.squareup.moshi.Json

data class PetDetailResponse(
    @Json(name = "animal") val animal: AnimalResponse
)