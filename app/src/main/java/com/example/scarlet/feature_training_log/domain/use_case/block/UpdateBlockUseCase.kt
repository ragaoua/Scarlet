package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class UpdateBlockUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Update a block after checking that the name isn't blank
     *
     * @param block block to be updated
     *
     * @return a resource with an error if found, or a simple resource with no data
     */
    suspend operator fun invoke(block: Block): SimpleResource {
        if (block.name.isBlank()) {
            return Resource.Error(StringResource(R.string.error_block_name_is_empty))
        }

        repository.updateBlock(block)
        return Resource.Success()
    }
}