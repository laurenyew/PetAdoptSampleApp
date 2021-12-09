package laurenyew.petadoptsampleapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.data.models.PetListResult
import laurenyew.petadoptsampleapp.data.networking.commands.SearchPetsCommands
import retrofit2.HttpException
import java.io.IOException

// PetAdopt page API is 1 based
private const val PETADOPT_STARTING_PAGE_INDEX = 1

class PetSearchPagingSource(
    private val searchPetCommand: SearchPetsCommands,
    private val query: String
) : PagingSource<Int, Animal>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Animal> {
        val position = params.key ?: PETADOPT_STARTING_PAGE_INDEX
        return try {
            when (val result =
                searchPetCommand.searchForNearbyPets(query, position, params.loadSize)) {
                is PetListResult.Success -> {
                    val animals = result.animals
                    val currentPage = result.page.currentPage
                    val nextKey = if (animals.isEmpty()) {
                        null
                    } else {
                        currentPage + 1
                    }
                    LoadResult.Page(
                        data = animals,
                        prevKey = if (position == PETADOPT_STARTING_PAGE_INDEX) null else currentPage - 1,
                        nextKey = nextKey
                    )
                }
                is PetListResult.Error -> {
                    LoadResult.Error(result.exception)
                }
            }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    /**
     * The refresh key is used for the initial load of the next PagingSource, after invalidation
     * Use the closest page to the current position
     */
    override fun getRefreshKey(state: PagingState<Int, Animal>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

}