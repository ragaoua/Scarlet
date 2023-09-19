package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.use_case.block.helpers.ValidateBlockNameHelper

class UpdateBlockUseCase(
    private val repository: ScarletRepository,
    private val validateBlockName: ValidateBlockNameHelper
) {

    /**
     * Update a block after checking that the name isn't blank
     *
     * @param block block to be updated
     *
     * @return a resource with an error if found, or a simple resource with no data
     */
    suspend operator fun invoke(block: Block): SimpleResource {
        validateBlockName(block.name).let { resource ->
            resource.error?.let {
                return Resource.Error(resource.error)
            }
        }

        repository.updateBlock(block)
        return Resource.Success()
    }
}