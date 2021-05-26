package laurenyew.petadoptsampleapp.repository.networking.api

import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.AGE_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.BREED_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.COAT_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.COLOR_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.DISTANCE_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.GENDER_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.IS_CAT_FRIENDLY_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.IS_CHILD_FRIENDLY_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.IS_DOG_FRIENDLY_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.LOCATION_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.ORG_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.PAGE_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.RESULT_NUM_LIMIT_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.SEARCH_PETS_METHOD
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.STATUS_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.PetAdoptApiConstants.Search.TYPE_PARAM
import laurenyew.petadoptsampleapp.repository.networking.api.responses.SearchPetsNetworkResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PetFinderApi {
    /**
     * Search pets using the given query parameters)
     */
    @Throws(RuntimeException::class)
    @GET(SEARCH_PETS_METHOD)
    suspend fun searchPets(
        @Query(TYPE_PARAM) type: String? = null,
        @Query(BREED_PARAM) breed: String? = null,
        @Query(GENDER_PARAM) gender: String? = null,
        @Query(AGE_PARAM) age: String? = null,
        @Query(COLOR_PARAM) color: String? = null,
        @Query(COAT_PARAM) coat: String? = null,
        @Query(STATUS_PARAM) status: String? = null,
        @Query(ORG_PARAM) organization: String? = null,
        @Query(IS_CHILD_FRIENDLY_PARAM) isGoodWithChildren: Boolean? = null,
        @Query(IS_DOG_FRIENDLY_PARAM) isGoodWithDogs: String? = null,
        @Query(IS_CAT_FRIENDLY_PARAM) isGoodWithCats: String? = null,
        @Query(LOCATION_PARAM) location: String? = null,
        @Query(DISTANCE_PARAM) distance: Int? = null,
        @Query(PAGE_PARAM) page: Int? = null,
        @Query(RESULT_NUM_LIMIT_PARAM) resultLimit: Int? = null
    ): Response<SearchPetsNetworkResponse?>
}