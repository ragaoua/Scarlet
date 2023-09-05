package com.example.scarlet.feature_training_log.domain.repository

import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithDays
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovement
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.domain.model.Set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestRepository: ScarletRepository {

    private val blocks = mutableListOf<Block>()
    private val days = mutableListOf<DayWithSessions<Session>>()
    private val sessions = mutableListOf<Session>()

    override suspend fun insertBlockWithDays(block: Block): Long {
        val blockToBeInserted = block.copy(id = (blocks.size+1).toLong())

        blocks.add(blockToBeInserted)

        return blockToBeInserted.id
    }

    override suspend fun updateBlock(block: Block) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBlock(block: Block) {
        TODO("Not yet implemented")
    }

    override fun getAllBlocks(): Flow<List<BlockWithDays<DayWithSessions<Session>>>> {
        return flow { emit(
            blocks.map { block ->
                BlockWithDays(
                    id = block.id,
                    name = block.name,
                    days = days.filter { day -> day.blockId == block.id }
                )
            }
        )}
    }

    override suspend fun getBlockByName(name: String): Block? {
        return blocks.find { it.name == name }
    }

    override suspend fun insertDay(day: Day): Long {
        val dayToBeInserted = day.copy(id = (days.size+1).toLong())

        days.add(
            DayWithSessions(
                id = dayToBeInserted.id,
                blockId = dayToBeInserted.blockId,
                name = dayToBeInserted.name,
                order = dayToBeInserted.order,
                sessions = emptyList()
            )
        )

        return dayToBeInserted.id
    }

    override suspend fun getDaysByBlockId(blockId: Long): List<Day> {
        TODO("Not yet implemented")
    }

    override suspend fun insertSession(session: Session): Long {
        val sessionToBeInserted = session.copy(id = (sessions.size+1).toLong())

        sessions.add(sessionToBeInserted)

        return sessionToBeInserted.id
    }

    override suspend fun updateSession(session: Session) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSession(session: Session) {
        TODO("Not yet implemented")
    }

    override fun getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockId(blockId: Long):
            Flow<List<DayWithSessions<SessionWithExercises<ExerciseWithMovement>>>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertExercise(exercise: Exercise): Long {
        TODO("Not yet implemented")
    }

    override suspend fun updateExercise(exercise: Exercise) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        TODO("Not yet implemented")
    }

    override fun getExercisesWithMovementAndSetsBySessionId(sessionId: Long):
            Flow<List<ExerciseWithMovementAndSets>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertSet(set: Set): Long {
        TODO("Not yet implemented")
    }

    override suspend fun updateSet(set: Set) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSet(set: Set) {
        TODO("Not yet implemented")
    }

    override suspend fun insertMovement(movement: Movement): Long {
        TODO("Not yet implemented")
    }

    override fun getAllMovements(): Flow<List<Movement>> {
        TODO("Not yet implemented")
    }
}