package laurenyew.petadoptsampleapp.db.favorite

import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.db.animal.AnimalDatabaseProvider

interface FavoriteAnimalDatabaseProvider: AnimalDatabaseProvider {
    suspend fun getAllFavoriteAnimals(): List<Animal>

    suspend fun isAnimalFavorited(id: String): Boolean

    suspend fun favoriteAnimal(animal: Animal)

    suspend fun unFavoriteAnimal(animal: Animal)
}