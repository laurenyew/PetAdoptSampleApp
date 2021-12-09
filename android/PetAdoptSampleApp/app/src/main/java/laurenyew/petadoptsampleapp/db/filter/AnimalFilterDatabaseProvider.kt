package laurenyew.petadoptsampleapp.db.filter

interface AnimalFilterDatabaseProvider {
    suspend fun animalFilter(): AnimalFilter?

    suspend fun updateAnimalFilter(animal: AnimalFilter)
}