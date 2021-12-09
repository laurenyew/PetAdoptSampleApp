package laurenyew.petadoptsampleapp.db.search

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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