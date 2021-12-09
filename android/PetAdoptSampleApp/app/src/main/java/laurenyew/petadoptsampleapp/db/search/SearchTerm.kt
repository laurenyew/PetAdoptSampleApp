package laurenyew.petadoptsampleapp.db.search

import androidx.room.*

@Entity
data class SearchTerm(
    @PrimaryKey val searchId: String,
    val zipcode: String,
    val timestamp: Long = System.currentTimeMillis()
)

