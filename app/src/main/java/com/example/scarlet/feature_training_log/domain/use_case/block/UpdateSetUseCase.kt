package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class UpdateSetUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Update a set
     *
     * @param set set to be updated
     */
    suspend operator fun invoke(set: Set) {
        repository.updateSet(set)
    }
}