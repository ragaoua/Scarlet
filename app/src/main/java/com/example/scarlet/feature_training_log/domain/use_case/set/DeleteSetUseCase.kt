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
     *
     * Note: this won't be unit tested because it's a simple call to the repository
     */
    suspend operator fun invoke(set: Set) {
        repository.deleteSetAndUpdateSubsequentSetsOrder(set)
    }
}