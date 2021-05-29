package laurenyew.petadoptsampleapp.database.search

import androidx.room.*

@Entity
data class SearchTerm(
    @PrimaryKey val searchId: String,
    val zipcode: String,
    val timestamp: Long
)

@Dao
interface SearchTermDao {
    @Query("SELECT * from searchterm ORDER by timestamp DESC")
    suspend fun getAllSearchTerms(): List<SearchTerm>

    @Query("SELECT * from searchterm WHERE searchId = :searchId")
    suspend fun getSearchTerm(searchId: String): SearchTerm?

    @Query("DELETE from searchterm where searchId = :searchId")
    suspend fun deleteSearchTerm(searchId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchTerm: SearchTerm)
}