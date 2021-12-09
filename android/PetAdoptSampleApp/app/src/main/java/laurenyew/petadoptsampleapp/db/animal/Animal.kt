package laurenyew.petadoptsampleapp.db.animal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class Animal(
    @PrimaryKey val animalId: Long,
    val orgId: String? = null,
    val species: String? = null,
    val name: String? = null,
    val sex: String? = null,
    val age: String? = null,
    val size: String? = null,
    val description: String? = null,
    val status: String? = null,
    val breed: String? = null,
    val photoUrl: String? = null,
    val distance: String? = null,
    val contact: Contact? = null,
    val isFavorite: Boolean = false,
    val index: Long
) {
    fun copy(isFavorite: Boolean): Animal =
        Animal(
            animalId,
            orgId,
            species,
            name,
            sex,
            age,
            size,
            description,
            status, breed, photoUrl, distance, contact, isFavorite,
            index
        )

    fun isDog(): Boolean = species.equals("Dog", true)
    fun isCat(): Boolean = species.equals("Cat", true)
    fun isFemale(): Boolean = sex.equals("Female", true)
    fun isMale(): Boolean = sex.equals("Male", true)
}

@JsonClass(generateAdapter = true)
data class Contact(
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null
)