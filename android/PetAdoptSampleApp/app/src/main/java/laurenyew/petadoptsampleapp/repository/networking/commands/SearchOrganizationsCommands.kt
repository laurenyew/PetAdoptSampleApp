package laurenyew.petadoptsampleapp.repository.networking.commands

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import laurenyew.petadoptsampleapp.database.organization.Organization
import laurenyew.petadoptsampleapp.database.search.SearchTerm
import laurenyew.petadoptsampleapp.repository.networking.api.PetFinderApi
import laurenyew.petadoptsampleapp.repository.networking.api.responses.OrganizationsResponse
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SearchOrganizationsCommands @Inject constructor(
    private val api: PetFinderApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun searchForNearbyOrganizations(searchTerm: SearchTerm): List<Organization> =
        withContext(ioDispatcher) {
            suspendCancellableCoroutine { cancellableContinuation ->
                Timber.d("Executing Search for nearby Organizations: ${searchTerm.zipcode}")
                fetchOrganizations(searchTerm, object : SearchOrganizationsCallback {
                    override fun onSuccess(organizations: List<Organization>) {
                        cancellableContinuation.resume(organizations)
                    }

                    override fun onFailure(exception: Exception) {
                        cancellableContinuation.resumeWithException(exception)
                    }
                })
            }
        }

    private fun fetchOrganizations(searchTerm: SearchTerm, callback: SearchOrganizationsCallback) {
        val call = api.searchOrganizations(location = searchTerm.zipcode)
        try {
            val response = call.execute()
            val result = parseResponse(searchTerm.searchId, response)
            callback.onSuccess(result)
        } catch (exception: Exception) {
            callback.onFailure(exception)
        }
    }


    /**
     * Parse the response from the network call
     */
    @Throws(RuntimeException::class)
    private fun parseResponse(
        searchId: String,
        networkResponse: Response<OrganizationsResponse?>?
    ): List<Organization> {
        val data = networkResponse?.body()
        if (networkResponse?.code() != 200 || data == null) {
            val error = "API call failed. Response error: ${networkResponse?.errorBody()?.string()}"
            Timber.e(error)
            throw RuntimeException(error)
        } else {
            val organizationList = arrayListOf<Organization>()
            data.organizations.forEachIndexed { index, item ->
                organizationList.add(
                    item.toOrganization(searchId, index)
                )
            }
            Timber.d("Completed command with organization list: ${organizationList.size}")
            return organizationList
        }
    }

    interface SearchOrganizationsCallback {
        fun onSuccess(organizations: List<Organization>)
        fun onFailure(exception: Exception)
    }
}