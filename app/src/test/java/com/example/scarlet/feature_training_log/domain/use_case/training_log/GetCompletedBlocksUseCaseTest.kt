package com.example.scarlet.feature_training_log.domain.use_case.training_log

import com.example.scarlet.feature_training_log.domain.model.Block
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

class GetCompletedBlocksUseCaseTest {

    private lateinit var getCompletedBlocks: GetCompletedBlocksUseCase
    private lateinit var repository: ScarletRepository

    @Before
    fun setUp() {
        repository = TestRepository()
        getCompletedBlocks = GetCompletedBlocksUseCase(repository)

        val blockIds = mutableListOf<Long>()
        listOf(
            Block(completed = true),
            Block(completed = true),
            Block(completed = false),
            Block(completed = true),
            Block(completed = true),
            Block(completed = true),
            Block(completed = false)
        ).shuffled().forEach {
            runBlocking {
                repository.insertBlock(it)
                    .also { id -> blockIds.add(id) }
            }
        }

        (1..100).forEach { _ ->
            val session = Session(
                date = Date(System.currentTimeMillis() + (random()*1000000).toInt()),
                blockId = blockIds.random().toInt()
            )
            runBlocking {
                repository.insertSession(session)
            }
        }
    }

    @Test
    fun getCompletedBlockUseCase_returnsCorrectlySortedCompletedBlocks() = runBlocking {
        getCompletedBlocks().first()
            .data?.let { blocksWithSessions ->
                assertTrue(
                    "blocks are not all completed",
                    blocksWithSessions.all { it.block.completed }
                )

                assertTrue(
                    "Wrong number of blocks returned",
                    blocksWithSessions.size == 5
                )

                for (i in 1..blocksWithSessions.lastIndex) {
                    val block = blocksWithSessions[i]
                    val precedingBlock = blocksWithSessions[i-1]
                    println(precedingBlock.sessions.last())
                    println(block.sessions.last())
                    println(precedingBlock.sessions.last().date >= block.sessions.last().date)
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