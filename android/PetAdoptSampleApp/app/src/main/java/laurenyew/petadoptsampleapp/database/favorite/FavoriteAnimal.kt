package laurenyew.petadoptsampleapp.database.favorite

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Entity
data class FavoriteAnimal(
    @PrimaryKey val id: String,
    val name: String?,
    val photoUrl: String?,
    val age: String?,
    val sex: String?,
    val size: String?,
    val type: String?
)

@Dao
interface FavoriteAnimalDao {
    @Query("SELECT * from favoriteanimal")
    suspend fun getFavoriteAnimals(): List<FavoriteAnimal>

    @Query("SELECT * from favoriteanimal WHERE id = :id")
    suspend fun getFavoriteAnimal(id: String): FavoriteAnimal?

    @Query("DELETE from favoriteanimal where id = :id")
    suspend fun deleteFavoriteAnimal(id: String)

    @Insert(onConflict = REPLACE)
    suspend fun insert(favoriteAnimal: FavoriteAnimal)
}