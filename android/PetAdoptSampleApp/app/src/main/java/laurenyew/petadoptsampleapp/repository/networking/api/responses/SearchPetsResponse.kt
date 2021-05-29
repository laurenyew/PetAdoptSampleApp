package laurenyew.petadoptsampleapp.repository.networking.api.responses

import com.squareup.moshi.Json

//TODO: Use Jetpack Compose Pages
data class SearchPetsNetworkResponse(
    @Json(name = "animals") val animals: List<Animal>,
    @Json(name = "pagination") val pagination: Pagination
)

data class Animal(
    @Json(name = "id") val id: String,
    @Json(name = "organization_id") val organizationId: String?,
    @Json(name = "url") val url: String?,
    @Json(name = "type") val type: String?,
    @Json(name = "species") val species: String?,
    @Json(name = "breeds") val breeds: Breeds,
    @Json(name = "colors") val colors: Colors,
    @Json(name = "age") val age: String?,
    @Json(name = "gender") val gender: String?,
    @Json(name = "size") val size: String?,
    @Json(name = "coat") val coat: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "photos") val photos: List<Photo>,
    @Json(name = "status") val status: String?,
    @Json(name = "attributes") val attributes: Attributes,
    @Json(name = "environment") val environment: Environment,
    @Json(name = "tags") val tags: List<String>,
    @Json(name = "contact") val contact: Contact,
    @Json(name = "publishDate") val publishDate: String?,
    @Json(name = "distance") val distance: String?
)

data class Breeds(
    @Json(name = "primary") val primary: String?,
    @Json(name = "secondary") val secondary: String?,
    @Json(name = "mixed") val mixed: Boolean?,
    @Json(name = "unknown") val unknown: Boolean?
)

data class Colors(
    @Json(name = "primary") val primary: String?,
    @Json(name = "secondary") val secondary: String?,
    @Json(name = "tertiary") val tertiary: String?
)

data class Photo(
    @Json(name = "small") val smallUrl: String?,
    @Json(name = "medium") val mediumUrl: String?,
    @Json(name = "large") val largeUrl: String?,
    @Json(name = "full") val fullUrl: String?
)

data class Attributes(
    @Json(name = "spayed_neutered") val spayedNeutered: Boolean?,
    @Json(name = "house_trained") val houseTrained: Boolean?,
    @Json(name = "declawed") val declawed: Boolean?,
    @Json(name = "special_needs") val specialNeeds: Boolean?,
    @Json(name = "shots_current") val shotsCurrent: Boolean?
)

data class Environment(
    @Json(name = "children") val children: Boolean?,
    @Json(name = "dogs") val dogs: Boolean?,
    @Json(name = "cats") val cats: Boolean?
)

data class Contact(
    @Json(name = "email") val email: String?,
    @Json(name = "phone") val phone: String?,
    @Json(name = "address") val address: Address
)

data class Address(
    @Json(name = "address1") val address1: String?,
    @Json(name = "address2") val address2: String?,
    @Json(name = "city") val city: String?,
    @Json(name = "state") val state: String?,
    @Json(name = "postcode") val postcode: String?,
    @Json(name = "country") val country: String?
)

data class Pagination(
    @Json(name = "count_per_page") val countPerPage: String?,
    @Json(name = "total_count") val totalCount: Int,
    @Json(name = "current_page") val currentPage: Int,
    @Json(name = "total_pages") val totalPages: Int
)