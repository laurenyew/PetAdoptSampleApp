package laurenyew.petadoptsampleapp.data.networking.auth

/**
 * Provides an access token for request authorization.
 */
interface AccessTokenProvider {

    /**
     * Returns an access token. In the event that you don't have a token return null.
     * Automatically calls [refreshToken] if cached token is null.
     */
    fun token(): String?

    /**
     * Refreshes the token and returns it.
     * In the event that the token could not be refreshed return null.
     */
    suspend fun refreshToken(): String?
}