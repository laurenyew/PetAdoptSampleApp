package laurenyew.petadoptsampleapp.database.animal

interface AnimalDatabaseProvider {
    suspend fun getSearchedAnimalList(searchId: String): List<Animal>?

    suspend fun deleteSearchedAnimalList(searchId: String)

    suspend fun deleteAllSearchedAnimalLists()

    suspend fun insertSearchedAnimalList(searchId: String, list: List<Animal>)
}