package com.example.scarlet.feature_training_log.domain.use_case.training_log

import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.repository.TestRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.lang.Math.random
import java.util.Date

class GetAllBlocksUseCaseTest {

    private lateinit var getCompletedBlocks: GetAllBlocksUseCase
    private lateinit var repository: ScarletRepository

    @Before
    fun setUp() {
        repository = TestRepository()
        getCompletedBlocks = GetAllBlocksUseCase(repository)

        val dayIds = mutableListOf<Long>()
        (1..5).forEach { _ ->
            runBlocking {
                repository.insertBlock(Block())
                    .also { blockId ->
                        repository.insertDay(
                            Day(
                                blockId = blockId,
                                order = 1
                            )
                        ).also { dayId -> dayIds.add(dayId) }
                    }
            }
        }

        (1..100).forEach { _ ->
            val session = Session(
                date = Date(System.currentTimeMillis() + (random()*1000000).toInt()),
                dayId = dayIds.random()
            )
            runBlocking {
                repository.insertSession(session)
            }
        }
    }

    @Test
    fun getAllBlocksUseCase_returnsAllBlocksCorrectlySorted() = runBlocking {
        getCompletedBlocks().first()
            .data?.let { blocksWithSessions ->
                assertTrue(
                    "Wrong number of blocks returned",
                    blocksWithSessions.size == 5
                )

                for (i in 1..blocksWithSessions.lastIndex) {
                    val block = blocksWithSessions[i]
                    val precedingBlock = blocksWithSessions[i-1]
                    assertTrue(
                        "Wrong sorting of blocks",
                        precedingBlock.sessions.last().date >= block.sessions.last().date
                    )
                }

                blocksWithSessions.forEach {
                    for (i in 1..it.sessions.lastIndex) {
                        val s0 = it.sessions[i-1]
                        val s1 = it.sessions[i]
                        assertTrue(
                            "Wrong sorting of sessions for block ${it.block}",
                            s0.date <= s1.date
                        )
                    }
                }
            } ?: fail("No data returned")
    }
}