package com.example.scarlet.feature_training_log.domain.use_case.exercise

import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class GetNbExercisesByMovementIdUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Returns the number of exercises that use the given movement.
     *
     * @param movementId the id of the movement
     *
     * @return the number of exercises that use the given movement
     */
    suspend operator fun invoke(movementId: Long): Int {
        return repository.getNbExercisesByMovementId(movementId)
    }
}