package laurenyew.petadoptsampleapp.repository.networking.api.responses

import com.squareup.moshi.Json
import laurenyew.petadoptsampleapp.database.organization.Organization

data class OrganizationsResponse(
    @Json(name = "organizations") val organizations: List<OrganizationResponse>,
    @Json(name = "pagination") val pagination: Pagination
)

data class OrganizationResponse(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "email") val email: String?,
    @Json(name = "phone") val phone: String?,
    @Json(name = "url") val url: String?,
    @Json(name = "address") val address: Address,
    @Json(name = "photos") val photos: List<Photo>,
) {
    fun toOrganization(searchId: String, index: Int): Organization {
        val photo = if (photos.isNotEmpty()) {
            photos[0].fullUrl
        } else {
            null
        }

        return Organization(
            orgId = id,
            searchId = searchId,
            name = name,
            email = email,
            phone = phone,
            url = url,
            address = address.address1 + "\n"
                    + address.city + ", "
                    + address.state + ", "
                    + address.postcode,
            photo = photo,
            index = index,
        )
    }
}