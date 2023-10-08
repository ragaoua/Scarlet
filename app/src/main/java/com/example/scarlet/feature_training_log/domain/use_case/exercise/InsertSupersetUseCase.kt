package com.example.scarlet.feature_training_log.domain.use_case.exercise

import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class InsertSupersetUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Insert a list of exercises that share the same order.
     * That order is defined by the number of exercises for the session.
     *
     * @param sessionId id of the exercise's session
     * @param movementIds list of movement ids for the exercises
     */
    suspend operator fun invoke(sessionId: Long, movementIds: List<Long>) {
        repository.insertExercisesWhileSettingOrder(
            movementIds.map {
                Exercise(
                    sessionId = sessionId,
                    movementId = it
                )
            }
        )
    }
}