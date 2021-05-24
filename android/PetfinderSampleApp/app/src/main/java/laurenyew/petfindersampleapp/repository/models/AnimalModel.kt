package laurenyew.petfindersampleapp.repository.models

data class AnimalModel(
    val id: String,
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
    val contact: ContactModel? = null,
    val isFavorite: Boolean = false
) {
    fun copy(isFavorite: Boolean): AnimalModel =
        AnimalModel(
            id,
            orgId,
            type,
            name,
            sex,
            age,
            size,
            description,
            status, breed, photoUrl, distance, contact, isFavorite
        )
}

data class ContactModel(
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null
)