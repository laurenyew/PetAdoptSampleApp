package laurenyew.petadoptsampleapp.database.search

import laurenyew.petadoptsampleapp.repository.models.AnimalModel

interface SearchAnimalListDatabaseProvider {
    suspend fun getSearchedAnimalList(searchId: String): List<AnimalModel>?

    suspend fun deleteSearchedAnimalList(searchId: String)

    suspend fun insertSearchedAnimalList(searchId: String, list: List<AnimalModel>)
}