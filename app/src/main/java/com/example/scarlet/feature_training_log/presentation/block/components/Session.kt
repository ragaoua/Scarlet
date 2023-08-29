package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import com.example.scarlet.feature_training_log.presentation.core.DateUtils
import com.example.scarlet.feature_training_log.presentation.core.components.TitledLazyList
import com.example.scarlet.ui.theme.TitleLazyListPadding

@Composable
fun Session(
    session: SessionWithExercises<ExerciseWithMovementAndSets>,
    onEvent: (BlockEvent) -> Unit
) {
    TitledLazyList(
        modifier = Modifier
            .fillMaxWidth()
            .padding(TitleLazyListPadding),
        title = DateUtils.formatDate(session.date)
    ) {
        if(session.exercises.isNotEmpty()) {
            items(session.exercises) { exercise ->
                Text(text = exercise.movement.name)
                // TODO
//                ExerciseCard(
//                    exercise = exercise,
//                    isInEditMode = state.isInEditMode,
//                    onEvent = onEvent
//                )
            }
        } else {
            item {
                Text(
                    text = stringResource(R.string.empty_session),
                    style = MaterialTheme.typography.bodyMedium
                    // TODO color = grey
                )
            }
        }
        // TODO
 //        if (!state.isInEditMode) {
//            item {
//                Button(
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = MaterialTheme.shapes.small,
//                    onClick = {
//                        onEvent(SessionEvent.ShowMovementSelectionSheet())
//                    }
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription = stringResource(R.string.new_exercise)
//                    )
//                }
//            }
//        }
    }
}