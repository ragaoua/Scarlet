package com.example.scarlet.feature_training_log.domain.use_case.set

import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class DeleteSetUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Delete a set and update the order of the subsequent sets for the same exercise
     *
     * @param set set to be deleted
     */
    suspend operator fun invoke(set: Set) {
        repository.deleteSetAndUpdateSubsequentSetsOrder(set)
    }
}