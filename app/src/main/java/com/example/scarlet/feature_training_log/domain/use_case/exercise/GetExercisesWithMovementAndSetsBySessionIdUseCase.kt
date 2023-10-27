package com.example.scarlet.feature_training_log.domain.use_case.exercise

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class GetExercisesWithMovementAndSetsBySessionIdUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Retrieve a list of exercises for a given session
     *
     * @param sessionId id of the exercises' session
     *
     * @return a flow of resources with data (the list of exercises)
     *
     * Note: this won't be unit tested because it's a simple call to the repository
     */
    suspend operator fun invoke(sessionId: Long):
            Resource<List<ExerciseWithMovementAndSets>> {
        return repository.getExercisesWithMovementAndSetsBySessionId(sessionId)
            .let { Resource.Success(it) }
    }
}