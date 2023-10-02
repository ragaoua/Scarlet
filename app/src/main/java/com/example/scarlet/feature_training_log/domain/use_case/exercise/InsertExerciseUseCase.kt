package com.example.scarlet.feature_training_log.domain.use_case.exercise

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class InsertExerciseUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Insert an exercise.
     * The order is defined by the number of exercises for the session.
     *
     * @param sessionId id of the exercise's session
     * @param movementId id of the exercise's movement
     *
     * @return a resource with data (id of the inserted exercise)
     */
    suspend operator fun invoke(sessionId: Long, movementId: Long): Resource<Long> {
        return repository.insertExerciseWhileSettingOrder(
            Exercise(
                sessionId = sessionId,
                movementId = movementId
            )
        )
            .let { Resource.Success(it) }
    }
}