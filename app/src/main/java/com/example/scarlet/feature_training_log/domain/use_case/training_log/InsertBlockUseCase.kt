package com.example.scarlet.feature_training_log.domain.use_case.training_log

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class InsertBlockUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(block: Block): Resource<Long> {
        // TODO : check if the block name is valid (not empty and not already used)
        return Resource.Success(
            repository.insertBlock(block)
        )
    }

}