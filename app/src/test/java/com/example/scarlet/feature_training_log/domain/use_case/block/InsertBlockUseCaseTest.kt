package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.R
import com.example.scarlet.feature_training_log.data.repository.TestRepository
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.use_case.block.helpers.ValidateBlockNameHelper
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Insert a block with an empty name and check that an error is returned
 * Insert a block with a name that already exists and check that an error is returned
 * Insert a block with a name that doesn't exist and check that an id is returned
 *
 * @see InsertBlockUseCase
 */
class InsertBlockUseCaseTest {

    private val repository: ScarletRepository = TestRepository()
    private val insertBlock = InsertBlockUseCase(
        repository = repository,
        validateBlockName = ValidateBlockNameHelper(repository)
    )

    init {
        runBlocking {
            repository.insertBlockWithDays(
                block = Block(name = "Block A"),
                days = emptyList()
            )
        }
    }

    @Test
    fun insertingBlockWithEmptyName_returnsError() = runBlocking {
        val error = insertBlock(blockName = "", nbDays = 1).error
        assertTrue(error?.resId == R.string.error_block_name_is_empty)
    }

    @Test
    fun insertingBlockWithAlreadyUsedName_returnsError() = runBlocking {
        val error = insertBlock(blockName = "Block A", nbDays = 1).error
        assertTrue(error?.resId == R.string.block_with_name_already_exists)
    }

    @Test
    fun insertBlock_returnsId() = runBlocking {
        val id = insertBlock(blockName = "MyBlock", nbDays = 3).data
        assertTrue(id != null)
    }
}