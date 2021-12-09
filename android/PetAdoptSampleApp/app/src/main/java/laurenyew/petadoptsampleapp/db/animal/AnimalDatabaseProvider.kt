package laurenyew.petadoptsampleapp.db.animal

interface AnimalDatabaseProvider {
    suspend fun getAnimals(): List<Animal>

    suspend fun getAnimal(id: String): Animal?

    suspend fun deleteAnimal(id: String)

    suspend fun updateAnimal(animal: Animal)

    suspend fun deleteAllAnimals()
}