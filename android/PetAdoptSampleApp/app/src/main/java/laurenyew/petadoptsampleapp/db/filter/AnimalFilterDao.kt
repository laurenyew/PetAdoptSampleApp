package laurenyew.petadoptsampleapp.db.filter

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AnimalFilterDao {
    @Query("SELECT * from AnimalFilter")
    suspend fun getAnimalFilters(): List<AnimalFilter>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(filter: AnimalFilter)
}