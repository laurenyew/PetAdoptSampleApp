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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val searchTermsList =
        viewModel.searchTermsList.collectAsStateLifecycleAware(initial = emptyList())
    val lastPollTime =
        viewModel.lastPollTime.collectAsStateLifecycleAware(initial = "")
    Column(Modifier.fillMaxSize()) {
        SearchTermsList(searchTermsList)
        Divider(color = dividerColor)
        PollInfoCard(lastPollTime)
    }
}

@Composable
fun SearchTermsList(searchTerms: State<List<SearchTerm>>) {
    val searchTermItems = searchTerms.value
    val dateFormat = remember {
        SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.US)
    }
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
                text = "- ${item.zipcode}, lastSearch: ${dateFormat.format(Date(item.timestamp))} ",
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

@Composable
fun PollInfoCard(pollTimeState: State<String>) {
    val pollTimeString = pollTimeState.value
    if (pollTimeString.isEmpty()) {
        Text(
            text = "No last poll.",
            modifier = Modifier
                .padding(8.dp)
        )
    } else {
        Text(
            text = "Last poll time: $pollTimeString",
            modifier = Modifier
                .padding(8.dp)
        )
    }
    Divider(color = dividerColor)
}

@Preview
@Composable
fun PollInfoCardPreview() {
    val pollTimeState = remember {
        mutableStateOf("")
    }
    PollInfoCard(pollTimeState = pollTimeState)
}