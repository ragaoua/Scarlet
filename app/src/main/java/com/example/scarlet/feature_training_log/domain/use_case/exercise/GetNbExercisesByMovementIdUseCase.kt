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
     *
     * Note: this won't be unit tested because it's a simple call to the repository
     */
    suspend operator fun invoke(movementId: Long): Int {
        return repository.getNbExercisesByMovementId(movementId)
    }
}