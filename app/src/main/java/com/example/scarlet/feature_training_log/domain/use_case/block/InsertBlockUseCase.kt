package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.use_case.block.helpers.ValidateBlockNameHelper

class InsertBlockUseCase(
    private val repository: ScarletRepository,
    private val validateBlockName: ValidateBlockNameHelper
) {

    /**
     * Insert a block (and its blocks) after checking that its name isn't blank or used already.
     *
     * @param blockName the block's name
     * @param nbDays number of days to insert for the block
     *
     * @return resource with an error or data (the inserted block)
     */
    suspend operator fun invoke(blockName: String, nbDays: Int): Resource<Block> {
        validateBlockName(blockName).let { resource ->
            resource.error?.let {
                return Resource.Error(resource.error)
            }
        }

        var block = Block(name = blockName)
        val days = (1..nbDays).map {
            Day(
                name = "Day $it", // No need to localize this string, this is just a placeholder
                order = it
            )
        }

        repository.insertBlockWithDays(block, days)
            .also { block = block.copy(id = it) }

        return Resource.Success(block)
    }

}