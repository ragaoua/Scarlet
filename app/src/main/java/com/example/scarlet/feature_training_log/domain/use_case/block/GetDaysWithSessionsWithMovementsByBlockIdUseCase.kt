package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.DayWithSessionsWithExercisesWithMovementName
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
    operator fun invoke(blockId: Long): Flow<Resource<List<DayWithSessionsWithExercisesWithMovementName>>> {
        return repository.getDaysWithSessionsWithExercisesWithMovementNameByBlockId(blockId)
            .map { list ->
                list.map { dayWithSessionWithExercisesWithMovementName ->
                    dayWithSessionWithExercisesWithMovementName.copy(
                        sessions = dayWithSessionWithExercisesWithMovementName.sessions
                            .sortedByDescending { it.session.date }
                            .map { sessionWithExercisesWithMovementNames ->
                                sessionWithExercisesWithMovementNames.copy(
                                    exercises = sessionWithExercisesWithMovementNames.exercises
                                        .sortedBy { it.exercise.order }
                                )
                            }
                    )
                }.let { Resource.Success(it) }
            }
    }
}