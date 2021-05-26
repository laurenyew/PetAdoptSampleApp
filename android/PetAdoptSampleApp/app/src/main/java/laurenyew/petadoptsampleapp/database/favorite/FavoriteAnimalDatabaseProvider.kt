package laurenyew.petadoptsampleapp.database.favorite

interface FavoriteAnimalDatabaseProvider {
    suspend fun getAllFavoriteAnimals(): List<FavoriteAnimal>

    suspend fun getFavoriteAnimal(id: String): FavoriteAnimal?

    suspend fun isAnimalFavorited(id: String): Boolean

    suspend fun favoriteAnimal(animal: FavoriteAnimal)

    suspend fun unFavoriteAnimal(id: String)
}