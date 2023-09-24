package com.example.scarlet.feature_training_log.domain.use_case.movement

import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class DeleteMovementUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Delete a movement
     *
     * @param movement movement to be deleted
     */
    suspend operator fun invoke(movement: Movement) {
        repository.deleteMovement(movement)
    }

}