package laurenyew.petfindersampleapp.repository.networking.commands

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import laurenyew.petfindersampleapp.repository.networking.api.PetfinderTokenApi
import laurenyew.petfindersampleapp.repository.networking.api.requests.AuthTokenRequestBody
import laurenyew.petfindersampleapp.repository.networking.api.responses.RefreshApiTokenNetworkResponse
import laurenyew.petfindersampleapp.repository.responses.RefreshTokenRepoResponse
import laurenyew.petfindersampleapp.utils.ControlledRunner
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class AuthCommands @Inject constructor(
    private val api: PetfinderTokenApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    // Only let one job run at a time.
    private val refreshTokenControlledRunner = ControlledRunner<RefreshTokenRepoResponse>()

    suspend fun refreshToken(clientId: String, clientSecret: String): RefreshTokenRepoResponse =
        refreshTokenControlledRunner.joinPreviousOrRun {
            withContext(ioDispatcher) {
                Timber.d("Executing $REFRESH_TOKEN_TAG")
                val call = api.refreshToken(
                    AuthTokenRequestBody(
                        clientId = clientId,
                        clientSecret = clientSecret
                    )
                )
                val response = call.execute()
                parseResponse(response)
            }
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
    }
}