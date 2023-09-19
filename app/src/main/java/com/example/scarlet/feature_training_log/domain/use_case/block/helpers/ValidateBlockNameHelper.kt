package com.example.scarlet.feature_training_log.domain.use_case.block.helpers

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class ValidateBlockNameHelper(
    private val repository: ScarletRepository
) {

    /**
     * Check that a block name isn't blank or used already.
     *
     * @param blockName the block name
     *
     * @return simple resource
     */
    suspend operator fun invoke(blockName: String): SimpleResource {
        if (blockName.isBlank()) {
            return Resource.Error(StringResource(R.string.error_block_name_is_empty))
        }

        repository.getBlockByName(blockName)?.let { existingBlock ->
            return Resource.Error(
                StringResource(R.string.block_with_name_already_exists, existingBlock.name)
            )
        }

        return Resource.Success()
    }

}