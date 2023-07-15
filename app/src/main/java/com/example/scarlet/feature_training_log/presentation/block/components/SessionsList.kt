package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import com.example.scarlet.feature_training_log.presentation.components.DeletableItem
import com.example.scarlet.feature_training_log.presentation.components.TitledLazyList
import com.example.scarlet.feature_training_log.presentation.destinations.SessionScreenDestination
import com.example.scarlet.ui.theme.MainButtonContentPadding
import com.example.scarlet.ui.theme.TitleLazyListPadding
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator


@Composable
fun SessionsList(
    navigator: DestinationsNavigator,
    sessions: Map<Session, List<Movement>>,
    isEditing: Boolean,
    onEvent: (BlockEvent) -> Unit
) {
    TitledLazyList(
        modifier = Modifier
            .fillMaxWidth()
            .padding(TitleLazyListPadding),
        title = stringResource(R.string.block_sessions_list_title)
    ) {
        items(sessions.keys.toList()) { session ->
            val sessionMovements = sessions[session]!!
            Button(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = MainButtonContentPadding,
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                onClick = {
                    navigator.navigate(SessionScreenDestination(session = session))
                },
                enabled = !isEditing
            ) {
                DeletableItem (
                    modifier = Modifier.fillMaxSize(),
                    onDeleteClicked = {
                        onEvent(BlockEvent.DeleteSession(session))
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = session.date,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = if (sessionMovements.isNotEmpty()) {
                                sessionMovements.joinToString { it.name }
                            } else {
                                stringResource(R.string.empty_session)
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoSessionsPreview() {
    SessionsList(
        navigator = EmptyDestinationsNavigator,
        sessions = emptyMap(),
        isEditing = false,
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SessionsSectionPreview() {
    SessionsList(
        navigator = EmptyDestinationsNavigator,
        sessions = mapOf(
            Session(date = "2021-01-01") to emptyList(),
            Session(date = "2021-01-02") to emptyList(),
            Session(date = "2021-01-03") to listOf(
                Movement(name = "Squat"),
                Movement(name = "Bench Press"),
                Movement(name = "Deadlift"),
                Movement(name = "Front Squat"),
                Movement(name = "Dips"),
            ),
        ),
        isEditing = false,
        onEvent = {}
    )
}