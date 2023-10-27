package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.data.repository.TestRepository
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.use_case.block.helpers.ValidateBlockNameHelper
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Update a block with an empty name and check that an error is returned
 * Update a block with a name that already exists and check that an error is returned
 * Update a block with a name that doesn't exist and check that an id is returned
 *
 * @see UpdateBlockUseCase
 */
class UpdateBlockUseCaseTest {

    private val repository: ScarletRepository = TestRepository()
    private val updateBlock = UpdateBlockUseCase(
        repository = repository,
        validateBlockName = ValidateBlockNameHelper(repository)
    )
    private var block: Block = Block(name = "Block A")

    init {
        runBlocking {
            repository.insertBlockWithDays(
                block = Block(name = "Block A"),
                days = emptyList()
            ).let {
                block = block.copy(id = it)
            }
        }
    }

    @Test
    fun updateBlockWithEmptyName_returnsError() = runBlocking {
        val error = updateBlock(block.copy(name = "")).error

        assertTrue(error?.resId == R.string.error_block_name_is_empty)
    }

    @Test
    fun updateBlockWithAlreadyUsedName_returnsError() = runBlocking {
        val error = updateBlock(block.copy(name = "Block A")).error
        assertTrue(error?.resId == R.string.block_with_name_already_exists)
    }

    @Test
    fun updateBlock_returnsSuccess() = runBlocking {
        val resource = updateBlock(block.copy(name = "MyBlock"))
        assertTrue(resource is Resource.Success)
    }
}