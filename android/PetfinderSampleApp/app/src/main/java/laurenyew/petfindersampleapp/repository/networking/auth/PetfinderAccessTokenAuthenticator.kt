package laurenyew.petfindersampleapp.repository.networking.auth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * OKHTTP Authenticator for PetFinder Access token
 */
class PetfinderAccessTokenAuthenticator(
    private val tokenProvider: AccessTokenProvider
) : Authenticator {
    /**
     * Provide token information to authenticate
     * If no auth header is provided, add one with the given token
     * If an auth header is provided, try refreshing the token
     * If the token is refreshed, use that refreshed token
     * Otherwise, give up.
     */
    override fun authenticate(route: Route?, response: Response): Request? {
        val token = tokenProvider.token() ?: return null
        if (!hasAuthHeader(response)) {
            return response.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            val updatedToken = tokenProvider.refreshToken()
            if (updatedToken != token) {
                return response.request()
                    .newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $updatedToken")
                    .build()
            }
        }
        return null
    }


    private fun hasAuthHeader(response: Response): Boolean =
        response.request().header("Authorization") != null

}