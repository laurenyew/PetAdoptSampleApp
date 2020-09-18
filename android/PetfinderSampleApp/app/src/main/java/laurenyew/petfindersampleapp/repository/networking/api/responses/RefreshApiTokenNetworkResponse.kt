package laurenyew.petfindersampleapp.repository.networking.api.responses

import com.squareup.moshi.Json

data class RefreshApiTokenNetworkResponse(
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "expires_in") val expirationInSeconds: Int,
    @Json(name = "access_token") val accessToken: String
)