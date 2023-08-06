package com.example.scarlet.feature_training_log.domain.use_case.training_log

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class InsertBlockUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(block: Block): Resource<Long> {
        if (block.name.isBlank()) {
            return Resource.Error(
                StringResource(resId = R.string.error_block_name_is_empty)
            )
        }

        repository.getBlockByName(block.name)?.let { existingBlock ->
            return Resource.Error(
                StringResource(resId = R.string.block_with_name_already_exists, existingBlock.name)
            )
        }

        repository.insertBlock(block)
            .also { insertedBlockId ->
                return Resource.Success(insertedBlockId)
            }
    }

}