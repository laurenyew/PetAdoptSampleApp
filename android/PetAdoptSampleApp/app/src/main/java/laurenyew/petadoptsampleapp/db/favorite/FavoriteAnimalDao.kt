package laurenyew.petadoptsampleapp.db.favorite

import androidx.room.*
import laurenyew.petadoptsampleapp.db.animal.Animal

@Dao
interface FavoriteAnimalDao {
    @Query("SELECT * from animal WHERE isFavorite = 1")
    suspend fun getFavoriteAnimals(): List<Animal>
}