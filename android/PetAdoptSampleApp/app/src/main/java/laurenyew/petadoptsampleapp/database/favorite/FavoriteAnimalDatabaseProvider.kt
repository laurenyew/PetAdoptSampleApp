package laurenyew.petadoptsampleapp.database.favorite

import laurenyew.petadoptsampleapp.database.animal.Animal

interface FavoriteAnimalDatabaseProvider {
    suspend fun getAllFavoriteAnimals(): List<FavoriteAnimal>

    suspend fun getFavoriteAnimal(id: String): FavoriteAnimal?

    suspend fun isAnimalFavorited(id: String): Boolean

    suspend fun favoriteAnimal(animal: FavoriteAnimal)

    suspend fun updateFavoritedAnimal(animal: FavoriteAnimal)

    suspend fun unFavoriteAnimal(id: String)
}