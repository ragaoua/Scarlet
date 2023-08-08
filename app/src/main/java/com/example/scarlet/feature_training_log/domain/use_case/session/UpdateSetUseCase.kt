package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class UpdateSetUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(set: Set) {
        repository.updateSet(set)
    }
}