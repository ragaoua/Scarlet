package com.example.scarlet.feature_training_log.domain.use_case.training_log

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class InsertBlockUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Insert a block after checking that its name isn't blank or used already.
     * Then, insert the days for the block
     *
     * @param blockName the block's name
     * @param nbDays number of days to insert for the block
     *
     * @return resource with an error or data (the inserted block)
     */
    suspend operator fun invoke(blockName: String, nbDays: Int): Resource<Block> {
        if (blockName.isBlank()) {
            return Resource.Error(StringResource(R.string.error_block_name_is_empty))
        }

        repository.getBlockByName(blockName)?.let { existingBlock ->
            return Resource.Error(
                StringResource(R.string.block_with_name_already_exists, existingBlock.name)
            )
        }

        var block = Block(name = blockName)
        repository.insertBlock(block)
            .also { block = block.copy(id = it) }

        (1..nbDays).forEach {
            repository.insertDay(Day(
                blockId = block.id,
                name = "Day $it", // No need to localize this string, this is just a placeholder
                order = it
            ))
        }

        return Resource.Success(block)
    }

}