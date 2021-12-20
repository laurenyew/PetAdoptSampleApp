package laurenyew.petadoptsampleapp.ui.features.petList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.flow.Flow
import laurenyew.petadoptsampleapp.R
import laurenyew.petadoptsampleapp.db.animal.Animal
import laurenyew.petadoptsampleapp.ui.features.list.ListItem
import laurenyew.petadoptsampleapp.ui.theme.dividerColor

@Composable
fun PetList(
    isRefreshing: State<Boolean>,
    animals: Flow<PagingData<Animal>>,
    onRefresh: () -> Unit,
    onItemClicked: (id: Long) -> Unit,
    onItemFavorited: (item: Animal, isFavorited: Boolean) -> Unit
) {
    val items = animals.collectAsLazyPagingItems()
    val isRefreshingState = SwipeRefreshState(isRefreshing.value)
    SwipeRefresh(state = isRefreshingState, onRefresh = { onRefresh() }) {
        LazyColumn {
            items(items.itemCount) { index ->
                val item = items[index]
                item?.let {
                    PetListItem(
                        item = item,
                        onItemClicked = { id -> onItemClicked(id) },
                        onItemFavorited = { isFavorited ->
                            onItemFavorited(item, isFavorited)
                        }
                    )
                    Divider(color = dividerColor)
                }
            }
        }
    }
}


@Composable
fun PetListItem(
    item: Animal,
    onItemClicked: (Long) -> Unit,
    onItemFavorited: (Boolean) -> Unit
) {
    val unknown = stringResource(id = R.string.unknown)
    val age = item.age ?: unknown
    val sex = item.sex ?: unknown
    val size = item.size ?: unknown
    val title = item.name ?: unknown
    val description = stringResource(id = R.string.basic_info_formatted_string, age, sex, size)
    val isFavorite = remember(item) {
        mutableStateOf(item.isFavorite)
    }

    ListItem(
        imageUrl = item.photoUrl,
        title = title,
        description = description,
        isFavorite = isFavorite,
        onItemFavorited = onItemFavorited,
        modifier = Modifier
            .clickable(onClick = { onItemClicked(item.animalId) })
    )
}


@Preview
@Composable
fun PetListItemPreview() {
    val animalModel = Animal(
        animalId = 12345L, name = "Fido", species = "Dog",
        sex = "Male",
        age = "7 years",
        size = "Large",
        description = "A great dog is an amazing companion that loves to play ball and fetch and give kisses\nLoves to play ball",
        status = "Ready for Adoption",
        breed = "Mixed",
        photoUrl = null,
        distance = null,
        contact = null,
        orgId = null,
        index = 1
    )

    PetListItem(
        item = animalModel,
        onItemClicked = { },
        onItemFavorited = {}
    )
}