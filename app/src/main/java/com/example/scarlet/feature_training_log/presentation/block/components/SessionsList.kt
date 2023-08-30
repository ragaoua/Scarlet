package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import com.example.scarlet.feature_training_log.presentation.block.BlockUiState


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SessionsList(
    state: BlockUiState,
    onEvent: (BlockEvent) -> Unit
) {
    AnimatedContent(
        targetState = state.days.find { it.toDay() == state.selectedDay },
        label = "day selection animation"
    ) { day ->
        val lazyListState = rememberLazyListState()
        LazyRow(
            state = lazyListState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState),
            horizontalArrangement = Arrangement.Center
        ) {
            val sessions = day?.sessions ?: emptyList()
            if (sessions.isNotEmpty()) {
                items(sessions) { session ->
                    Session(
                        modifier = Modifier.fillParentMaxWidth(),
                        session = session,
                        isInSessionEditMode = state.isInSessionEditMode,
                        onEvent = onEvent
                    )
                }
            } else {
                item {
                    Text(
                        text = stringResource(R.string.no_sessions_yet),
                        style = MaterialTheme.typography.bodyMedium
                        // TODO use a lighter color
                    )
                }
            }
        }
    }
}