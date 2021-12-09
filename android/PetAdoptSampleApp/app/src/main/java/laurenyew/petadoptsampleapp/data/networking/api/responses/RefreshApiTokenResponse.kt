package laurenyew.petadoptsampleapp.data.networking.api.responses

import com.squareup.moshi.Json

data class RefreshApiTokenResponse(
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "expires_in") val expirationInSeconds: Int,
    @Json(name = "access_token") val accessToken: String
)