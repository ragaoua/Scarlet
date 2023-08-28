package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.animation.AnimatedContent
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
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import com.example.scarlet.feature_training_log.presentation.block.BlockUiState
import com.example.scarlet.feature_training_log.presentation.core.DateUtils
import com.example.scarlet.feature_training_log.presentation.core.components.DeletableItem
import com.example.scarlet.feature_training_log.presentation.core.components.TitledLazyList
import com.example.scarlet.feature_training_log.presentation.destinations.SessionScreenDestination
import com.example.scarlet.ui.theme.MainButtonContentPadding
import com.example.scarlet.ui.theme.TitleLazyListPadding
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Composable
fun SessionsList(
    navigator: DestinationsNavigator,
    state: BlockUiState,
    onEvent: (BlockEvent) -> Unit
) {
    AnimatedContent(
        targetState = state.days.find { it.toDay() == state.selectedDay },
        label = "day selection animation"
    ) { day ->
        val sessions = day?.sessions ?: emptyList()
        TitledLazyList(
            modifier = Modifier
                .fillMaxWidth()
                .padding(TitleLazyListPadding),
            title = stringResource(R.string.block_sessions_list_title)
        ) {
            if (sessions.isNotEmpty()) {
                items(sessions) { sessions ->
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = MainButtonContentPadding,
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        onClick = {
                            navigator.navigate(
                                SessionScreenDestination(
                                    session = sessions.toSession(),
                                    block = state.block
                                )
                            )
                        }
                    ) {
                        DeletableItem(
                            modifier = Modifier.fillMaxSize(),
                            onDeleteClicked = {
                                onEvent(
                                    BlockEvent.DeleteSession(
                                        sessions.toSession()
                                    )
                                )
                            }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = DateUtils.formatDate(
                                        sessions.date
                                    ),
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = if (
                                        sessions.exercises.isNotEmpty()
                                    ) {
                                        sessions.exercises.joinToString {
                                            it.movement.name
                                        }
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
            } else {
                item {
                    Text(
                        text = stringResource(R.string.empty_block),
                        style = MaterialTheme.typography.bodyMedium
                        // TODO color = grey
                    )
                }
            }
        }
    }
}