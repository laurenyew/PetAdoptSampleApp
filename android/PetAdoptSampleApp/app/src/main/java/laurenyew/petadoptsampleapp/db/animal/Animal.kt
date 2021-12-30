package laurenyew.petadoptsampleapp.db.animal

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.squareup.moshi.JsonClass

@Entity
@TypeConverters(ContactTypeConverter::class)
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

class ContactTypeConverter {
    @TypeConverter
    fun toString(contact: Contact?): String? =
        contact?.let { contact ->
            val stringBuilder = StringBuilder()
            contact.email?.let {
                stringBuilder.append(it)
            }
            stringBuilder.append(",")
            contact.phone?.let {
                stringBuilder.append(it)
            }
            stringBuilder.append(",")
            contact.address?.let {
                stringBuilder.append(it)
            }
            stringBuilder.toString()
        }

    @TypeConverter
    fun toContact(contactString: String?): Contact? {
        return contactString?.let {
            val parts: List<String> = it.split(",")
            if (parts.size != 3) {
                throw RuntimeException("Invalid Contact")
            } else {
                return Contact(
                    toContactPart(parts[0]),
                    toContactPart(parts[1]),
                    toContactPart(parts[2])
                )
            }
        }
    }

    private fun toContactPart(part: String): String? =
        part.ifEmpty {
            null
        }
}