package laurenyew.petadoptsampleapp.database.search

import androidx.room.*
import laurenyew.petadoptsampleapp.database.animal.Animal

@Entity
data class SearchAnimalList(
    @PrimaryKey val searchId: String,
    val animalList: List<Animal>
)

@Dao
interface SearchAnimalListDao {
    @Query("SELECT * from searchanimallist WHERE searchId = :searchId")
    suspend fun getSearchedAnimalList(searchId: String): SearchAnimalList?

    @Query("DELETE from searchanimallist where searchId = :searchId")
    suspend fun deleteSearchedAnimalList(searchId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchAnimalList: SearchAnimalList)
}