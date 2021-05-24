package laurenyew.petfindersampleapp.repository.models

data class AnimalModel(
    val id: String,
    val orgId: String?,
    val type: String?,
    val name: String?,
    val sex: String?,
    val age: String?,
    val size: String?,
    val description: String?,
    val status: String?,
    val breed: String?,
    val photoUrl: String?,
    val distance: String?,
    val contact: ContactModel?,
    var isFavorite: Boolean = false
)

data class ContactModel(
    val email: String?,
    val phone: String?,
    val address: String?
)