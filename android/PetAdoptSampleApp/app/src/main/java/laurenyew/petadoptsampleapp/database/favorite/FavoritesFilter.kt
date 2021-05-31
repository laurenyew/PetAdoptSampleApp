package laurenyew.petadoptsampleapp.ui.features.favorites;

import androidx.room.*
import kotlin.random.Random

/**
 * There is only ever one favorites filter
 */
@Entity
data class FavoritesFilter(
    @PrimaryKey val id: Long = Random.nextLong(),
    var showFemales: Boolean = true,
    var showMales: Boolean = true,
    var showDogs: Boolean = true,
    var showCats: Boolean = true
) {
    fun isFiltering(): Boolean =
        !showFemales || !showMales || !showDogs || !showCats

    override fun toString(): String {
        return "FavoritesFilter{ " +
                "showFemales: $showFemales, showMales: $showMales," +
                "showDogs: $showDogs, showCats: $showCats," +
                "}"
    }
}

@Dao
interface FavoriteFilterDao {
    @Query("SELECT * from favoritesfilter")
    suspend fun getFavoriteFilters(): List<FavoritesFilter>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoritesFilter: FavoritesFilter)
}