package laurenyew.petadoptsampleapp.db.animal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface AnimalDao {
    @Query("SELECT * from animal")
    suspend fun getAnimals(): List<Animal>

    @Query("SELECT * from animal WHERE animalId = :id")
    suspend fun getAnimal(id: String): Animal?

    @Query("DELETE from animal where animalId = :id")
    suspend fun deleteAnimal(id: String)

    @Insert(onConflict = REPLACE)
    suspend fun insert(animal: Animal)

    @Query("DELETE FROM animal")
    suspend fun deleteAllAnimals()
}