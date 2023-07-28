package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercisesWithMovementName
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSessionsWithMovementsByBlockIdUseCase(
    private val repository: ScarletRepository
) {
    operator fun invoke(blockId: Int): Flow<Resource<List<SessionWithExercisesWithMovementName>>> {
        return repository.getSessionsWithExercisesWithMovementNameByBlockId(blockId)
            .map { Resource.Success(
                it.sortedByDescending { sessionWithExercisesWithMovementNames ->
                    sessionWithExercisesWithMovementNames.session.date
                }.map { sessionWithExercisesWithMovementNames ->
                    sessionWithExercisesWithMovementNames.copy(
                        exercises = sessionWithExercisesWithMovementNames.exercises
                            .sortedBy { exerciseWithMovementName ->
                                exerciseWithMovementName.exercise.order
                            }
                    )
                }
            )}
    }
}