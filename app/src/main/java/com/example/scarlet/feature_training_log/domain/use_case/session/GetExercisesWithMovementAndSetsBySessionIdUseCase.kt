package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExercisesWithMovementAndSetsBySessionIdUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Retrieve a list of exercises for a given session
     *
     * @param sessionId id of the exercises' session
     *
     * @return a flow of resource with data (the list of exercises)
     */
    operator fun invoke(sessionId: Int): Flow<Resource<List<ExerciseWithMovementAndSets>>> {
        return repository.getExercisesWithMovementAndSetsBySessionId(sessionId)
            .map { exercisesWithMovementAndSets ->
                exercisesWithMovementAndSets
                    .sortedBy { it.exercise.order }
                    .let { Resource.Success(it) }
            }
    }
}