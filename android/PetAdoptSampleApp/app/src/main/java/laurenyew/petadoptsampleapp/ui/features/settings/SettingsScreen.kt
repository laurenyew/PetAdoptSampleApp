package laurenyew.petadoptsampleapp.ui.features.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import laurenyew.petadoptsampleapp.database.search.SearchTerm
import laurenyew.petadoptsampleapp.ui.theme.dividerColor
import laurenyew.petadoptsampleapp.utils.collectAsStateLifecycleAware

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val searchTermsList =
        viewModel.searchTermsList.collectAsStateLifecycleAware(initial = emptyList())
    Column(Modifier.fillMaxSize()) {
        SearchTermsList(searchTermsList)
        Divider(color = dividerColor)
    }
}

@Composable
fun SearchTermsList(searchTerms: State<List<SearchTerm>>) {
    val searchTermItems = searchTerms.value
    Text(
        text = "Searches so far:",
        style = MaterialTheme.typography.body1,
        modifier = Modifier
            .padding(8.dp)
    )
    LazyColumn {
        items(searchTermItems.size) { index ->
            val item = searchTermItems[index]
            Text(
                text = "- ${item.zipcode}",
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun SearchTermsListPreview() {
    val searchTerms = remember {
        mutableStateOf(
            listOf(
                SearchTerm("78759", "78759"),
                SearchTerm("78737", "78737")
            )
        )
    }
    SearchTermsList(searchTerms)
}