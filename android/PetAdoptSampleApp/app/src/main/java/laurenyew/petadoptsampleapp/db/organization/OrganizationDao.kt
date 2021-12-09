package laurenyew.petadoptsampleapp.db.organization

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OrganizationDao {
    @Query("SELECT * from organization WHERE searchId = :searchId ORDER by `index` ASC")
    suspend fun getOrganizations(searchId: String): List<Organization>

    @Query("DELETE FROM organization")
    suspend fun deleteAllOrganizations()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(organization: Organization)
}