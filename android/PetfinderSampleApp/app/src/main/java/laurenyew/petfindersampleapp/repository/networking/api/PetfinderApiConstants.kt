package laurenyew.petfindersampleapp.repository.networking.api

object PetfinderApiConstants {
    const val BASE_URL = "https://api.petfinder.com/v2/"

    //Paths
    const val SEARCH_PETS_METHOD = "animals"

    //Query Params
    const val TYPE_PARAM = "type"
    const val BREED_PARAM = "breed"
    const val SIZE_PARAM = "size"
    const val GENDER_PARAM = "gender"
    const val AGE_PARAM = "age"
    const val COLOR_PARAM = "color"
    const val COAT_PARAM = "coat"
    const val STATUS_PARAM = "status"
    const val NAME_PARAM = "name"
    const val ORG_PARAM = "organization"
    const val IS_CHILD_FRIENDLY_PARAM = "goodWithChildren"
    const val IS_DOG_FRIENDLY_PARAM = "goodWithDogs"
    const val IS_CAT_FRIENDLY_PARAM = "goodWithCats"
    const val LOCATION_PARAM =  "location"
    const val DISTANCE_PARAM = "distance"
    const val PAGE_PARAM = "page"
    const val RESULT_NUM_LIMIT_PARAM = "limit"
}