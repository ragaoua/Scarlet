package com.example.scarlet.feature_training_log.domain.repository

import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithDays
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.Exercise
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

    override suspend fun insertBlockWithDays(
        block: Block,
        days: List<Day>
    ): Long {
        val blockToBeInserted = if (block.id == 0L) {
            block.copy(id = (blocks.size + 1).toLong())
        } else block

        blocks.add(blockToBeInserted)

        days.forEachIndexed { index, day ->
            val dayToBeInserted = DayWithSessions(
                id = if (day.id == 0L) {
                    (days.size + 1).toLong()
                } else day.id,
                blockId = blockToBeInserted.id,
                order = index,
                sessions = emptyList<Session>()
            )
            this.days.add(dayToBeInserted)
        }

        return blockToBeInserted.id
    }

    override suspend fun updateBlock(block: Block) {
        val blockToBeUpdatedIndex = blocks.indexOfFirst { it.id == block.id }

        if (blockToBeUpdatedIndex == -1) {
            throw Exception("Block with id ${block.id} not found")
        }

        blocks[blockToBeUpdatedIndex] = block
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

    override suspend fun insertBlockWithDaysWithSessionsWithExercisesWithMovementAndSets(
        block: Block,
        days: List<Day>,
        sessions: List<Session>,
        exercises: List<Exercise>,
        movements: List<Movement>,
        sets: List<Set>
    ): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getDaysByBlockId(blockId: Long): List<Day> {
        TODO("Not yet implemented")
    }

    override fun getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockId(blockId: Long): Flow<List<DayWithSessions<SessionWithExercises<ExerciseWithMovementAndSets>>>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSession(session: Session) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSession(session: Session) {
        TODO("Not yet implemented")
    }

    override suspend fun getSessionsWithExercisesByDayId(dayId: Long): List<SessionWithExercises<Exercise>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertSessionWithExercises(
        session: Session,
        exercises: List<Exercise>
    ): Long {
        val sessionToBeInserted = session.copy(id = (sessions.size+1).toLong())

        sessions.add(sessionToBeInserted)

        return sessionToBeInserted.id
    }

    override suspend fun insertSessionWithExercisesWithMovementAndSets(
        session: Session,
        exercises: List<Exercise>,
        movements: List<Movement>,
        sets: List<Set>
    ): Long {
        TODO("Not yet implemented")
    }

    override suspend fun insertExercisesWhileSettingOrder(exercises: List<Exercise>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateExercise(exercise: Exercise) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteExerciseAndUpdateSubsequenceExercisesOrder(exercise: Exercise) {
        TODO("Not yet implemented")
    }

    override suspend fun getExercisesWithMovementAndSetsBySessionId(sessionId: Long): List<ExerciseWithMovementAndSets> {
        TODO("Not yet implemented")
    }

    override suspend fun insertExerciseWithMovementAndSets(
        exercises: Exercise,
        movements: Movement,
        sets: List<Set>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun insertSupersetExerciseWithMovementAndSets(
        exercises: Exercise,
        movements: Movement,
        sets: List<Set>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getNbExercisesByMovementId(movementId: Long): Int {
        TODO("Not yet implemented")
    }

    override suspend fun updateExerciseOrder(exercises: List<Exercise>, newOrder: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateExerciseSupersetOrder(exercise: Exercise, newSupersetOrder: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun insertSetWhileSettingOrder(set: Set): Long {
        TODO("Not yet implemented")
    }

    override suspend fun updateSet(set: Set) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSetAndUpdateSubsequentSetsOrder(set: Set) {
        TODO("Not yet implemented")
    }

    override suspend fun restoreSet(set: Set) {
        TODO("Not yet implemented")
    }

    override suspend fun insertMovement(movement: Movement): Long {
        TODO("Not yet implemented")
    }

    override suspend fun updateMovement(movement: Movement) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMovement(movement: Movement) {
        TODO("Not yet implemented")
    }

    override fun getAllMovements(): Flow<List<Movement>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovementByName(name: String): Movement? {
        TODO("Not yet implemented")
    }
}