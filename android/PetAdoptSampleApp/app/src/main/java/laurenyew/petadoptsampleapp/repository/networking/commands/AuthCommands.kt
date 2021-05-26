package laurenyew.petadoptsampleapp.repository.networking.commands

import laurenyew.petadoptsampleapp.repository.networking.api.TokenApi
import laurenyew.petadoptsampleapp.repository.networking.api.requests.AuthTokenRequestBody
import laurenyew.petadoptsampleapp.repository.networking.api.responses.RefreshApiTokenNetworkResponse
import laurenyew.petadoptsampleapp.repository.responses.RefreshTokenRepoResponse
import laurenyew.petadoptsampleapp.utils.ControlledRunner
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class AuthCommands @Inject constructor(
    private val api: TokenApi
) {
    // Only let one job run at a time.
    private val refreshTokenControlledRunner = ControlledRunner<RefreshTokenRepoResponse>()

    suspend fun refreshToken(clientId: String, clientSecret: String): RefreshTokenRepoResponse =
        refreshTokenControlledRunner.joinPreviousOrRun {
            Timber.d("Executing $REFRESH_TOKEN_TAG")
            val response = api.refreshToken(
                AuthTokenRequestBody(
                    clientId = clientId,
                    clientSecret = clientSecret
                )
            )
            parseResponse(response)
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
        const val REFRESH_TOKEN_TAG: String = "refresh_petadopt_api_token"
    }
}