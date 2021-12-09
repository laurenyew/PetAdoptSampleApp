package laurenyew.petadoptsampleapp.data.networking.api.requests

import com.squareup.moshi.Json
import laurenyew.petadoptsampleapp.data.networking.api.PetAdoptApiConstants.Auth.CLIENT_ID_PARAM
import laurenyew.petadoptsampleapp.data.networking.api.PetAdoptApiConstants.Auth.CLIENT_SECRET_PARAM
import laurenyew.petadoptsampleapp.data.networking.api.PetAdoptApiConstants.Auth.GRANT_TYPE_PARAM
import laurenyew.petadoptsampleapp.data.networking.api.PetAdoptApiConstants.Auth.GRANT_TYPE_VALUE

data class AuthTokenRequestBody(
    @Json(name = GRANT_TYPE_PARAM)
    val grantType: String = GRANT_TYPE_VALUE,
    @Json(name = CLIENT_ID_PARAM)
    val clientId: String,
    @Json(name = CLIENT_SECRET_PARAM)
    val clientSecret: String
)