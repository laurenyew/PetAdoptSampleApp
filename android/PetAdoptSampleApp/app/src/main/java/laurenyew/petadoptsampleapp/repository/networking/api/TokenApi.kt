package laurenyew.petadoptsampleapp.repository.networking.api

import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Auth.REFRESH_TOKEN_METHOD
import laurenyew.petadoptsampleapp.repository.networking.api.requests.AuthTokenRequestBody
import laurenyew.petadoptsampleapp.repository.networking.api.responses.RefreshApiTokenNetworkResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenApi {
    /**
     * Refresh the Petfinder API Token (used to make any PetAdopt API request)
     */
    @Throws(RuntimeException::class)
    @POST(REFRESH_TOKEN_METHOD)
    suspend fun refreshToken(@Body tokenRequestBody: AuthTokenRequestBody): Response<RefreshApiTokenNetworkResponse?>
}