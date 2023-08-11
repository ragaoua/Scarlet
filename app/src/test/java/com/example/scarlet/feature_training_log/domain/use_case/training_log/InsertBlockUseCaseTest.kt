package com.example.scarlet.feature_training_log.domain.use_case.training_log

import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.repository.TestRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class InsertBlockUseCaseTest {

    private lateinit var insertBlock: InsertBlockUseCase
    private lateinit var repository: ScarletRepository

    @Before
    fun setUp() {
        repository = TestRepository()
        insertBlock = InsertBlockUseCase(repository)

        runBlocking {
            repository.insertBlock(Block(name = "Block A"))
        }
    }

    @Test
    fun insertBlockUseCase_insertsBlock() {
        val blockWithEmptyName = Block(name = "")
        runBlocking {
            insertBlock(blockWithEmptyName).also { resource ->
                resource.error?.let { error ->
                    assertTrue(
                        "Wrong resource id returned",
                        error.resId == R.string.error_block_name_is_empty
                    )
                } ?: fail("No error returned when inserting a block with an empty name")
            }
        }

        val existingBlock = Block(name = "Block A")
        runBlocking {
            insertBlock(existingBlock).also { resource ->
                resource.error?.let { error ->
                    assertTrue(
                        "Wrong resource id returned",
                        error.resId == R.string.block_with_name_already_exists
                    )
                } ?: fail("No error returned when inserting a block with an already used name")
            }
        }

        val validBlock = Block(name = "MyBlock")
        runBlocking {
            insertBlock(validBlock).also { resource ->
                resource.data ?: fail("No id returned when inserting")
                // TODO test that the block has been inserted ?
            }
        }
    }
}