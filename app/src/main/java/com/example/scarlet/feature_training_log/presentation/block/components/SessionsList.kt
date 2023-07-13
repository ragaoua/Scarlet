package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import com.example.scarlet.feature_training_log.presentation.components.ScarletListItem
import com.example.scarlet.feature_training_log.presentation.components.TitledLazyList
import com.example.scarlet.feature_training_log.presentation.destinations.SessionScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator


@Composable
fun SessionsList(
    navigator: DestinationsNavigator,
    sessions: List<Session>,
    onEvent: (BlockEvent) -> Unit
) {
    TitledLazyList(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        title = stringResource(R.string.block_sessions_list_title)
    ) {
        items(sessions) { session ->
            ScarletListItem(
                onClick = {
                    navigator.navigate(SessionScreenDestination(session = session))
                },
                onDelete = {
                    onEvent(BlockEvent.DeleteSession(session))
                }
            ) {
                Text(text = session.date)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoSessionsPreview() {
    SessionsList(
        navigator = EmptyDestinationsNavigator,
        sessions = emptyList(),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SessionsSectionPreview() {
    SessionsList(
        navigator = EmptyDestinationsNavigator,
        sessions = listOf(
            Session(date = "2021-01-01"),
            Session(date = "2021-01-02"),
            Session(date = "2021-01-03"),
            Session(date = "2021-01-04"),
            Session(date = "2021-01-05")
        ),
        onEvent = {}
    )
}