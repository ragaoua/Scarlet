package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class UpdateBlockUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(block: Block): SimpleResource {
        if (block.name.isBlank()) {
            return Resource.Error(Errors.BlockNameIsEmpty)
        }

        repository.updateBlock(block)
        return Resource.Success()
    }

    sealed interface Errors {
        object BlockNameIsEmpty: Errors
    }
}