package laurenyew.petfindersampleapp.repository.networking.auth

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import laurenyew.petfindersampleapp.BuildConfig
import laurenyew.petfindersampleapp.repository.networking.commands.AuthCommands
import laurenyew.petfindersampleapp.repository.responses.RefreshTokenRepoResponse
import javax.inject.Inject

class PetfinderTokenRepository @Inject constructor(
    private val authCommands: AuthCommands,
    context: Context?
) : AccessTokenProvider {
    private val coroutineContext = Dispatchers.Default + Job()
    private val sharedPreferences =
        context?.getSharedPreferences(ACCESS_TOKEN_SHARED_PREFS, Context.MODE_PRIVATE)

    override fun token(): String? = cachedToken() ?: runBlocking { refreshToken() }

    override suspend fun refreshToken(): String? =
        withContext(coroutineContext) {
            when (val response =
                authCommands.refreshToken(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET)) {
                is RefreshTokenRepoResponse.Success -> {
                    storeToken(response.token, response.expirationDate)
                    response.token
                }
                is RefreshTokenRepoResponse.Error -> {
                    Log.e(TAG, "Failed to refresh API token: " + response.error?.localizedMessage)
                    null
                }
                else -> null
            }
        }

    /**
     * Return the cached token if it's within the expiration time
     * Otherwise, clear the token if it exists, and return null
     */
    private fun cachedToken(): String? =
        sharedPreferences?.getString(TOKEN_KEY, null)?.let { token ->
            val currentDate = System.currentTimeMillis()
            val expirationDate =
                sharedPreferences.getLong(EXPIRATION_DATE, INVALID_DATE) ?: INVALID_DATE
            if (currentDate < expirationDate) {
                token
            } else {
                clearToken()
                null
            }
        }

    /**
     * Store the token and expiration date in shared prefs for now
     */
    private fun storeToken(token: String, expirationDate: Long) {
        sharedPreferences?.edit()?.apply {
            putString(TOKEN_KEY, token)
            putLong(EXPIRATION_DATE, expirationDate)
        }?.apply()
    }

    /**
     * Clear the token and expiration date from shared prefs
     */
    private fun clearToken() {
        sharedPreferences?.edit()?.apply {
            remove(TOKEN_KEY)
            remove(EXPIRATION_DATE)
        }?.apply()
    }

    companion object {
        //TODO: Not terribly secure to put access token in shared prefs, but leaving here for now to test
        private const val ACCESS_TOKEN_SHARED_PREFS = "access_token_shared_prefs"
        private const val TOKEN_KEY = "access_token_key"
        private const val EXPIRATION_DATE = "expiration_date"

        private const val INVALID_DATE = -1L

        val TAG: String = PetfinderTokenRepository::class.java.simpleName
    }
}