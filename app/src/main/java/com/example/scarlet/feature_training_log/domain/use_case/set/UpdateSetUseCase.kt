package com.example.scarlet.feature_training_log.domain.use_case.set

import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class UpdateSetUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Update a set
     *
     * @param set set to be updated
     *
     * Note: this won't be unit tested because it's a simple call to the repository
     */
    suspend operator fun invoke(set: Set) {
        repository.updateSet(set)
    }
}