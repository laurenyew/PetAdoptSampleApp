package laurenyew.petfindersampleapp.repository.networking.api.requests

import com.squareup.moshi.Json
import laurenyew.petfindersampleapp.repository.networking.api.PetfinderApiConstants.Auth.CLIENT_ID_PARAM
import laurenyew.petfindersampleapp.repository.networking.api.PetfinderApiConstants.Auth.CLIENT_SECRET_PARAM
import laurenyew.petfindersampleapp.repository.networking.api.PetfinderApiConstants.Auth.GRANT_TYPE_PARAM
import laurenyew.petfindersampleapp.repository.networking.api.PetfinderApiConstants.Auth.GRANT_TYPE_VALUE

data class AuthTokenRequestBody(
    @Json(name = GRANT_TYPE_PARAM)
    val grantType: String = GRANT_TYPE_VALUE,
    @Json(name = CLIENT_ID_PARAM)
    val clientId: String,
    @Json(name = CLIENT_SECRET_PARAM)
    val clientSecret: String
)