package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovement
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDaysWithSessionsWithMovementsByBlockIdUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Retrieve a list of days for a given block
     *
     * @param blockId id of the days' block
     *
     * @return a flow of resources with data (the list of days)
     */
    operator fun invoke(blockId: Long):
            Flow<Resource<List<DayWithSessions<SessionWithExercises<ExerciseWithMovement>>>>> {
        return repository.getDaysWithSessionsWithExercisesWithMovementByBlockId(blockId)
            .map { list ->
                list.map { day ->
                    day.copy(
                        sessions = day.sessions.sortedByDescending { it.date }
                            .map { session ->
                                session.copy(
                                    exercises = session.exercises.sortedBy { it.order }
                                )
                            }
                    )
                }.let { Resource.Success(it) }
            }
    }
}