package laurenyew.petadoptsampleapp.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import laurenyew.petadoptsampleapp.database.organization.Organization
import laurenyew.petadoptsampleapp.database.organization.OrganizationDatabaseProvider
import laurenyew.petadoptsampleapp.repository.networking.commands.SearchOrganizationsCommands
import laurenyew.petadoptsampleapp.repository.poll.PollManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrganizationSearchRepository @Inject constructor(
    private val searchOrganizationsCommands: SearchOrganizationsCommands,
    private val organizationDatabaseProvider: OrganizationDatabaseProvider,
    private val petSearchRepository: PetSearchRepository,
    pollManager: PollManager,
    externalScope: CoroutineScope
) {
    init {
        // Poll flow when we need to refresh
        pollManager.dataRefreshRequiredFlow
            .onEach {
                // Clear saved organizations
                organizationDatabaseProvider.deleteOrganizations()
            }.launchIn(externalScope)
    }

    suspend fun fetchOrganizationListFromLastSearch(): List<Organization> {
        val lastSearchTerm = petSearchRepository.getLastSearchTerm()
        return if (lastSearchTerm != null) {
            // Try using saved orgs first
            val savedOrgs = organizationDatabaseProvider.getOrganizations(lastSearchTerm.searchId)
            if (savedOrgs.isNotEmpty()) {
                savedOrgs
            } else {
                // Otherwise do the search on orgs and save the result
                val result = searchOrganizationsCommands
                    .searchForNearbyOrganizations(lastSearchTerm)
                organizationDatabaseProvider.insertOrganizations(result)
                result
            }
        } else {
            // No last search term
            emptyList()
        }
    }
}