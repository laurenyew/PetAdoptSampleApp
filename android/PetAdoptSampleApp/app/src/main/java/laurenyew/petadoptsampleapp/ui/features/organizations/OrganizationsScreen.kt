package laurenyew.petadoptsampleapp.ui.features.organizations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import laurenyew.petadoptsampleapp.database.organization.Organization
import laurenyew.petadoptsampleapp.ui.features.favorites.PetFiltersCard
import laurenyew.petadoptsampleapp.ui.features.list.ListItem
import laurenyew.petadoptsampleapp.ui.images.ImageState
import laurenyew.petadoptsampleapp.ui.images.loadPicture
import laurenyew.petadoptsampleapp.ui.theme.dividerColor
import laurenyew.petadoptsampleapp.ui.theme.sectionHeader
import laurenyew.petadoptsampleapp.utils.collectAsStateLifecycleAware
import timber.log.Timber

@Composable
fun OrganizationsScreen(viewModel: OrganizationsViewModel = hiltViewModel()) {
    val lastSearchTerm = viewModel.lastSearchTerm.collectAsStateLifecycleAware(initial = "")
    val lastSearchTermString = if (lastSearchTerm.value.isEmpty()) {
        "Unknown"
    } else {
        lastSearchTerm.value
    }

    val isLoading = viewModel.isLoading.collectAsStateLifecycleAware(initial = false)
    val items = viewModel.organizations.collectAsStateLifecycleAware(initial = emptyList())

    Column {
        Surface(
            elevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Last Search Term: $lastSearchTermString",
                style = MaterialTheme.typography.sectionHeader(),
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (isLoading.value) {
            CircularProgressIndicator(
                modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
        }
        OrganizationList(organizations = items,
            onItemClicked = { orgId ->
                Timber.d("Organization $orgId clicked")
            }
        )
    }
}

@Composable
fun OrganizationList(
    organizations: State<List<Organization>>,
    onItemClicked: (id: String) -> Unit,
) {
    val items = organizations.value
    LazyColumn {
        items(items.size) { index ->
            val item = items[index]
            val imageState = loadPicture(url = item.photo)
            OrganizationListItem(
                item = item,
                imageState = imageState.collectAsStateLifecycleAware(initial = ImageState.Empty),
                onItemClicked = { id -> onItemClicked(id) },
            )
            Divider(color = dividerColor)
        }
    }
}

@Composable
fun OrganizationListItem(
    item: Organization,
    imageState: State<ImageState>,
    onItemClicked: (id: String) -> Unit,
) {
    val title = item.name
    val stringBuilder = StringBuilder()
    item.email?.let {
        stringBuilder.append(it).append("\n")
    }
    item.phone?.let {
        stringBuilder.append(it)
    }
    val description = stringBuilder.toString()
    ListItem(
        imageState = imageState,
        title = title,
        description = description,
        modifier = Modifier.clickable { onItemClicked(item.orgId) }
    )
}