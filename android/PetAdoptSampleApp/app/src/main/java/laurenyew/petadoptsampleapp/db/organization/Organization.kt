package laurenyew.petadoptsampleapp.db.organization

import androidx.room.*

@Entity(primaryKeys = ["orgId", "searchId"])
data class Organization(
    val orgId: String,
    val searchId: String,
    val name: String,
    val email: String?,
    val phone: String?,
    val url: String?,
    val address: String?,
    val photo: String?,
    val index: Int
)

