package laurenyew.petfindersampleapp.repository.models

import java.net.URL

data class AnimalModel(
    val id: String,
    val name: String?,
    val photo: URL?
)