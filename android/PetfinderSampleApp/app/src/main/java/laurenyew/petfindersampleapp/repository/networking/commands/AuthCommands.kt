package laurenyew.petfindersampleapp.repository.networking.commands

import android.util.Log
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import laurenyew.petfindersampleapp.repository.networking.api.PetfinderTokenApi
import laurenyew.petfindersampleapp.repository.networking.api.requests.AuthTokenRequestBody
import laurenyew.petfindersampleapp.repository.networking.api.responses.RefreshApiTokenNetworkResponse
import laurenyew.petfindersampleapp.repository.responses.RefreshTokenRepoResponse
import retrofit2.Response
import javax.inject.Inject

class AuthCommands @Inject constructor(private val api: PetfinderTokenApi) : BaseNetworkCommand() {
    // Only let one job run at a time.
    private var deferredTokenJob: Deferred<RefreshTokenRepoResponse?>? = null

    suspend fun refreshToken(clientId: String, clientSecret: String): RefreshTokenRepoResponse? {
        if (deferredTokenJob == null || deferredTokenJob?.isActive == false) {
            deferredTokenJob = scope.async {
                Log.d(
                    TAG, "Executing $REFRESH_TOKEN_TAG"
                )
                val call = api.refreshToken(
                    AuthTokenRequestBody(
                        clientId = clientId,
                        clientSecret = clientSecret
                    )
                )
                try {
                    val response = call.execute()
                    parseResponse(response)
                } catch (e: Exception) {
                    null
                } finally {
                    //Clean up network call and cancel
                    call.cancel()
                }
            }
        }
        return deferredTokenJob?.await()
    }

    /**
     * Parse the response from the network call
     */
    private fun parseResponse(
        networkResponse: Response<RefreshApiTokenNetworkResponse?>?
    ): RefreshTokenRepoResponse {
        val data = networkResponse?.body()
        return if (networkResponse?.code() != 200 || data == null) {
            val error = RuntimeException(
                "API call failed. Response error: ${networkResponse?.errorBody()?.string()}"
            )
            RefreshTokenRepoResponse.Error(error)
        } else {
            val currentTime = System.currentTimeMillis()
            val expirationTime = data.expirationInSeconds.toLong() * SECONDS_TO_MILLIS
            val expirationDate = currentTime + expirationTime
            RefreshTokenRepoResponse.Success(data.accessToken, expirationDate)
        }
    }

    companion object {
        const val SECONDS_TO_MILLIS = 1000L

        const val REFRESH_TOKEN_TAG: String = "refresh_petfinder_api_token"
        val TAG: String = AuthCommands::class.java.simpleName
    }
}