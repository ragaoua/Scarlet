package com.example.scarlet.feature_training_log.domain.use_case.training_log

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class DeleteBlockUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(block: Block): SimpleResource {
        repository.deleteBlock(block)
        return Resource.Success()
    }

}