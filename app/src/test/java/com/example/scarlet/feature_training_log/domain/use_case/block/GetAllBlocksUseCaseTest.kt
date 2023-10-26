package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.isSortedBy
import com.example.scarlet.core.util.isSortedWith
import com.example.scarlet.feature_training_log.data.repository.TestRepository
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.lang.Math.random
import java.util.Date

/**
 * Insert blocks with days and sessions with random dates.
 * Some blocks don't have sessions.
 *
 * Check if the blocks are returned correctly sorted by latest session date descending
 * then by id descending.
 * Check if the sessions are returned correctly sorted by date.
 *
 * @see GetAllBlocksUseCase
 */
class GetAllBlocksUseCaseTest {

    private val repository: ScarletRepository = TestRepository()
    private var getAllBlocks = GetAllBlocksUseCase(repository)

    @Before
    fun setUp() = runBlocking {
        val nbBlocksWithSessions = 5
        val nbBlocksWithNoSessions = 2
        val nbDaysPerBlock = 3
        val nbSessions = 50

        // Insert blocks and days
        (1L..nbBlocksWithSessions).forEach { blockId ->
            repository.insertBlockWithDays(
                block = Block(id = blockId),
                days = (1L..nbDaysPerBlock).map { Day(id = it) }
            )
        }

        // Insert sessions
        (1..nbSessions).forEach { _ ->
            // Randomly assign a date but limit the choices to 5 possibilities.
            // Given the number of sessions (50) and blocks (5) inserted, at least 1 block
            // will have 2 sessions with the same date, which is what we want to test
            // (if 2 sessions have the same date, they should be sorted by id)
            val date = Date((random() * 4).toLong()*3600000)
            val session = Session(
                date = date,
                dayId = (random() * ((nbDaysPerBlock * nbBlocksWithSessions) - 1) + 1).toLong() // Randomly assign a day
            )
            repository.insertSessionWithExercises(session, emptyList())
        }

        // Insert a 4th and 5th block with no sessions
        (1..nbBlocksWithNoSessions).forEach { _ ->
            repository.insertBlockWithDays(
                block = Block(),
                days = (1..4).map { Day() }
            )
        }
    }

    @Test
    fun getAllBlocksUseCase_returnsAllBlocksCorrectlySorted() = runBlocking {
        getAllBlocks().first().data?.let { blocks ->
            val areBlockSortedByLatestSessionDescendingThenIdDescending =
                blocks.isSortedWith(
                    compareByDescending<BlockWithSessions<Session>> {
                        block -> block.sessions.maxOfOrNull { it.date }
                    }.thenByDescending { it.id }
                )
            if (!areBlockSortedByLatestSessionDescendingThenIdDescending) {
                fail("Blocks aren't sorted by latest session date descending")
            }

            blocks.forEach { block ->
                val areSessionsSortedByDate = block.sessions.isSortedBy { it.date }
                if (!areSessionsSortedByDate) {
                    fail("Sessions are not sorted by date for block $block")
                }
            }
        } ?: fail("Data is null")
    }
}