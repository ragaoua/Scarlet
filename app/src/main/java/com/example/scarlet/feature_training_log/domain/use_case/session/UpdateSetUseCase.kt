package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class UpdateSetUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(set: Set): SimpleResource {
        repository.updateSet(set)
        return Resource.Success()
    }
}