package laurenyew.petadoptsampleapp.repository.responses

sealed class RefreshTokenRepoResponse {
    data class Success(val token: String, val expirationDate: Long) : RefreshTokenRepoResponse()
    data class Error(val error: Throwable?) : RefreshTokenRepoResponse()
}