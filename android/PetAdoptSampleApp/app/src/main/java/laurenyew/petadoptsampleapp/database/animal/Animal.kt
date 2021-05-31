package laurenyew.petadoptsampleapp.database.animal

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Animal(
    val animalId: String,
    val orgId: String? = null,
    val type: String? = null,
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
    val isFavorite: Boolean = false
) {
    fun copy(isFavorite: Boolean): Animal =
        Animal(
            animalId,
            orgId,
            type,
            name,
            sex,
            age,
            size,
            description,
            status, breed, photoUrl, distance, contact, isFavorite
        )

    fun isDog(): Boolean = type.equals("Dog", true)
    fun isCat(): Boolean = type.equals("Cat", true)
    fun isFemale(): Boolean = sex.equals("Female", true)
    fun isMale(): Boolean = sex.equals("Male", true)
}

@JsonClass(generateAdapter = true)
data class Contact(
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null
)