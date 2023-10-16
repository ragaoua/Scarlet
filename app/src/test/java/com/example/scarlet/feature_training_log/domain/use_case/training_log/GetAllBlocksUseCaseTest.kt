package com.example.scarlet.feature_training_log.domain.use_case.training_log

import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.repository.TestRepository
import com.example.scarlet.feature_training_log.domain.use_case.block.GetAllBlocksUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.lang.Math.random
import java.util.Date

/**
 * Inserts 5 blocks with 4 days each and 20 sessions with random dates.
 * Some blocks don't have sessions.
 *
 * Checks if the blocks are returned in the correct number and correctly sorted.
 * Checks if the sessions are returned correctly sorted by date.
 *
 * @see GetAllBlocksUseCase
 */
class GetAllBlocksUseCaseTest {

    private val repository: ScarletRepository = TestRepository()
    private var getAllBlocks = GetAllBlocksUseCase(repository)

    @Before
    fun setUp() {
        runBlocking {
            var latestInsertedDayId = 0L
            (1..3).forEach { _ ->
                repository.insertBlockWithDays(
                    block = Block(),
                    days = (1..4).map { Day(id = ++latestInsertedDayId) }
                )
            }

            (1..20).forEach { _ ->
                val session = Session(
                    date = Date(System.currentTimeMillis() + (random() * 1000000).toInt()),
                    dayId = (random() * (latestInsertedDayId-1)).toLong() + 1 // Randomly assign a day
                )
                repository.insertSessionWithExercises(session, emptyList())
            }

            // Insert a 4th and 5th block with no sessions
            (4..5).forEach { _ ->
                repository.insertBlockWithDays(
                    block = Block(),
                    days = (1..4).map { Day(id = ++latestInsertedDayId) }
                )
            }
        }
    }

    @Test
    fun getAllBlocksUseCase_returnsAllBlocksCorrectlySorted() = runBlocking {
        getAllBlocks().first()
            .data?.let { blocksWithSessions ->
                assertTrue(
                    "Wrong number of blocks returned : ${blocksWithSessions.size}",
                    blocksWithSessions.size == 5
                )

                for (i in 1..blocksWithSessions.lastIndex) {
                    val blockLatestSession =
                        blocksWithSessions[i-1].days.flatMap { it.sessions }.maxOfOrNull { it.date }
                    val nextBlockLatestSession =
                        blocksWithSessions[i].days.flatMap { it.sessions }.maxOfOrNull { it.date }

                    // When both blocks share the same date for their latest session
                    // OR when both blocks have no sessions, compare block ids
                    if (blockLatestSession == nextBlockLatestSession) {
                        val blockId = blocksWithSessions[i-1].id
                        val nextBlockId = blocksWithSessions[i].id
                        assertTrue(
                            "Wrong sorting of blocks : both block share the same date " +
                                    "(or have no sessions) but the id is not descending",
                            blockId >= nextBlockId
                        )
                    } else {
                        assertTrue(
                            "Wrong sorting of blocks",
                            nextBlockLatestSession == null ||
                                    nextBlockLatestSession <= blockLatestSession
                        )
                    }
                }

                blocksWithSessions.forEach { block ->
                    val blockSessions = block.days.flatMap { day -> day.sessions }
                    for (i in 1..blockSessions.lastIndex) {
                        val session = blockSessions[i-1]
                        val nextSession = blockSessions[i]
                        assertTrue(
                            "Wrong sorting of sessions for block $block",
                            session.date <= nextSession.date
                        )
                    }
                }
            } ?: fail("No data returned")
    }
}