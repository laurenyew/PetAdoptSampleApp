package laurenyew.petadoptsampleapp.database.favorite

import laurenyew.petadoptsampleapp.ui.features.favorites.FavoritesFilter

interface FavoritesFilterDatabaseProvider {
    suspend fun getFavoritesFilter(): FavoritesFilter?

    suspend fun updateFavoritesFilter(favoritesFilter: FavoritesFilter)
}