package laurenyew.petadoptsampleapp.ui.features.favorites;

import androidx.room.*
import kotlin.random.Random

/**
 * There is only ever one favorites filter
 */
@Entity
data class FavoritesFilter(
    @PrimaryKey val id: Long = Random.nextLong(),
    val typeFilter: TypeFilter = TypeFilter(),
    val genderFilter: GenderFilter = GenderFilter(),
) {
    override fun toString(): String {
        return "FavoritesFilter{ TypeFilter { $typeFilter }, GenderFilter { $genderFilter } }"
    }
}

data class GenderFilter(
    var showFemales: Boolean = true,
    var showMales: Boolean = true,
) {
    override fun toString(): String {
        return "showFemales: $showFemales, showMales: $showMales"
    }
}

data class TypeFilter(
    var showDogs: Boolean = true,
    var showCats: Boolean = false
) {
    override fun toString(): String {
        return "showDogs: $showDogs, showCats: $showCats"
    }
}

@Dao
interface FavoriteFilterDao {
    @Query("SELECT * from favoritesfilter")
    suspend fun getFavoriteFilters(): List<FavoritesFilter>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoritesFilter: FavoritesFilter)
}