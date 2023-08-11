package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercisesWithMovementName
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSessionsWithMovementsByBlockIdUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Retrieve a list of sessions for a given block
     *
     * @param blockId id of the sessions' block
     *
     * @return a flow of resources with data (the list of sessions)
     */
    operator fun invoke(blockId: Long): Flow<Resource<List<SessionWithExercisesWithMovementName>>> {
        return repository.getSessionsWithExercisesWithMovementNameByBlockId(blockId)
            .map { list ->
                list.map { sessionWithExercisesWithMovementNames ->
                    sessionWithExercisesWithMovementNames.copy(
                        exercises = sessionWithExercisesWithMovementNames.exercises
                            .sortedBy { it.exercise.order }
                    )
                }.sortedByDescending { sessionWithExercisesWithMovementNames ->
                    sessionWithExercisesWithMovementNames.session.date
                }.let { Resource.Success(it) }
            }
    }
}