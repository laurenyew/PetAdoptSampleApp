package laurenyew.petfindersampleapp.networking.api

import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.AGE_PARAM
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.BREED_PARAM
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.COAT_PARAM
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.COLOR_PARAM
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.DISTANCE_PARAM
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.GENDER_PARAM
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.GET_PETS_METHOD
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.GET_SEARCH_PETS_METHOD
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.IS_CAT_FRIENDLY_PARAM
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.IS_CHILD_FRIENDLY_PARAM
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.IS_DOG_FRIENDLY_PARAM
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.LOCATION_PARAM
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.ORG_PARAM
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.PAGE_PARAM
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.RESULT_NUM_LIMIT_PARAM
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.SEARCH_PETS_METHOD
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.STATUS_PARAM
import laurenyew.petfindersampleapp.networking.api.PetfinderApiConstants.TYPE_PARAM
import laurenyew.petfindersampleapp.networking.api.responses.SearchPetsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PetfinderApi {
    @Throws(RuntimeException::class)
    @GET(SEARCH_PETS_METHOD)
    fun searchPets(
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
    ): Call<SearchPetsResponse?>?
}