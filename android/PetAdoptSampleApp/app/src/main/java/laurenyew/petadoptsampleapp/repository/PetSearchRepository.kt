package laurenyew.petadoptsampleapp.repository

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import laurenyew.petadoptsampleapp.repository.networking.commands.SearchPetsCommands
import laurenyew.petadoptsampleapp.repository.responses.SearchPetsRepoResponse
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

//TODO: Filters on pet search --> location, type, gender, etc. Save in Room DB (filter name?)
@Singleton
class PetSearchRepository @Inject constructor(
    private val searchPetCommand: SearchPetsCommands,
    private val sharedPreferences: SharedPreferences
) {
    private var lastSearchTerm = MutableStateFlow(getLastSearchTerm())

    suspend fun getNearbyDogs(location: String = lastSearchTerm.value): SearchPetsRepoResponse =
        try {
            val animals = searchPetCommand.searchForNearbyDogs(location)
            SearchPetsRepoResponse.Success(animals)
        } catch (e: Exception) {
            Timber.e(e)
            SearchPetsRepoResponse.Error(e)
        }

    fun getLastSearchTerm(): String =
        sharedPreferences.getString(LAST_SEARCH_ZIPCODE_KEY, "") ?: ""

    fun saveSearchTerms(zipcode: String) {
        sharedPreferences.edit()
            .putString(LAST_SEARCH_ZIPCODE_KEY, zipcode)
            .apply()
        lastSearchTerm.value = zipcode
    }

    companion object {
        private const val LAST_SEARCH_ZIPCODE_KEY = "last_search_zipcode_key"
    }
}