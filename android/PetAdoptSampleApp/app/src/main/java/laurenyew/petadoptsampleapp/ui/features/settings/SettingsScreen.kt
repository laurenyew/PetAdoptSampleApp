package laurenyew.petadoptsampleapp.ui.features.settings

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import laurenyew.petadoptsampleapp.db.search.SearchTerm
import laurenyew.petadoptsampleapp.ui.features.search.oldviewexample.PetSearchActivity
import laurenyew.petadoptsampleapp.ui.theme.dividerColor
import laurenyew.petadoptsampleapp.utils.collectAsStateLifecycleAware
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val searchTermsList =
        viewModel.searchTermsList.collectAsStateLifecycleAware(initial = emptyList())
    val lastPollTime =
        viewModel.lastPollTime.collectAsStateLifecycleAware(initial = "")
    val pollNum = viewModel.pollNumStateFlow.collectAsStateLifecycleAware(initial = 0)
    Column(Modifier.fillMaxSize()) {
        SearchTermsList(searchTermsList)
        Divider(color = dividerColor)
        PollInfoCard(
            pollTimeState = lastPollTime,
            pollNum = pollNum,
            onResetPollInterval = { newPollInterval ->
                viewModel.updatePollIntervalTime(newPollInterval)
            }
        )
        Divider(color = dividerColor)
        Button(
            onClick = {
                val activityIntent = Intent(context, PetSearchActivity::class.java)
                context.startActivity(activityIntent)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Open Example Search Pets with Traditional Fragment / Activity",
                style = MaterialTheme.typography.button
            )
        }
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
fun PollInfoCard(
    pollTimeState: State<String>,
    pollNum: State<Int>,
    onResetPollInterval: (Long?) -> Unit
) {
    val pollInterval: MutableState<Long> = remember {
        mutableStateOf(1000)
    }

    val pollTimeString = pollTimeState.value
    Column {

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
        Text(
            text = "Poll Num: ${pollNum.value}",
            modifier = Modifier.padding(8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "Poll Interval (millis):",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp)
            )
            TextField(
                value = pollInterval.value.toString(),
                onValueChange = {
                    pollInterval.value =
                        if (it.isEmpty()) {
                            -1L
                        } else {
                            it.toLong()
                        }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        ) {

            Button(
                onClick = { onResetPollInterval(pollInterval.value) },

                ) {
                Text(
                    text = "Update Poll Interval",
                    style = MaterialTheme.typography.button
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { onResetPollInterval(null) },

                ) {
                Text(
                    text = "Reset Poll Interval",
                    style = MaterialTheme.typography.button
                )
            }
        }
        Divider(color = dividerColor)
    }
}

@Preview
@Composable
fun PollInfoCardPreview() {
    val pollTimeState = remember {
        mutableStateOf("")
    }
    val pollNum = remember {
        mutableStateOf(0)
    }
    PollInfoCard(pollTimeState = pollTimeState, pollNum = pollNum, onResetPollInterval = {})
}