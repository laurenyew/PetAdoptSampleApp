package laurenyew.petfindersampleapp.repository.networking.api

import laurenyew.petfindersampleapp.repository.networking.api.PetfinderApiConstants.Auth.REFRESH_TOKEN_METHOD
import laurenyew.petfindersampleapp.repository.networking.api.requests.AuthTokenRequestBody
import laurenyew.petfindersampleapp.repository.networking.api.responses.RefreshApiTokenNetworkResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PetfinderTokenApi {
    /**
     * Refresh the Petfinder API Token (used to make any Petfinder API request)
     */
    @Throws(RuntimeException::class)
    @POST(REFRESH_TOKEN_METHOD)
    suspend fun refreshToken(@Body tokenRequestBody: AuthTokenRequestBody): Response<RefreshApiTokenNetworkResponse?>
}