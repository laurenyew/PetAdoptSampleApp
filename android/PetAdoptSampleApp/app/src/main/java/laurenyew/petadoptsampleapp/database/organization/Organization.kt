package laurenyew.petadoptsampleapp.database.organization

import androidx.room.*

@Entity(primaryKeys = ["orgId", "searchId"])
data class Organization(
    val orgId: String,
    val searchId: String,
    val name: String,
    val email: String?,
    val phone: String?,
    val url: String?,
    val address: String?,
    val photo: String?,
    val index: Int
)

@Dao
interface OrganizationDao {
    @Query("SELECT * from organization WHERE searchId = :searchId ORDER by `index` ASC")
    suspend fun getOrganizations(searchId: String): List<Organization>

    @Query("DELETE FROM organization")
    suspend fun deleteAllOrganizations()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(organization: Organization)
}