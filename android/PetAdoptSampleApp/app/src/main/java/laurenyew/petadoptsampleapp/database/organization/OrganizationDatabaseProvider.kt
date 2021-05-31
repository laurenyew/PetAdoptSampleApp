package laurenyew.petadoptsampleapp.database.organization

interface OrganizationDatabaseProvider {
    suspend fun getOrganizations(searchId: String): List<Organization>
    suspend fun deleteOrganizations()
    suspend fun insertOrganizations(organizations: List<Organization>)
}