package com.example.scarlet.feature_training_log.data.repository

import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestRepository: ScarletRepository {

    private val blocks = mutableListOf<Block>()
    private val days = mutableListOf<Day>()
    private val sessions = mutableListOf<Session>()
    private val exercises = mutableListOf<Exercise>()
    private val sets = mutableListOf<Set>()
    private val movements = mutableListOf<Movement>()

    override suspend fun insertBlockWithDays(
        block: Block,
        days: List<Day>
    ): Long {
        val blockToBeInserted = if (block.id == 0L) {
            block.copy(id = (blocks.size + 1).toLong())
        } else block

        blocks.add(blockToBeInserted)
        blocks.shuffle()

        days.forEachIndexed { index, day ->
            val dayToBeInserted = Day(
                id = if (day.id == 0L) {
                    (this.days.size + 1).toLong()
                } else day.id,
                blockId = blockToBeInserted.id,
                order = index + 1
            )
            this.days.add(dayToBeInserted)
        }
        this.days.shuffle()

        return blockToBeInserted.id
    }

    override suspend fun updateBlock(block: Block) {
        val indexOfUpdatedBlock = blocks.indexOfFirst { it.id == block.id }

        if (indexOfUpdatedBlock == -1) {
            throw Exception("Block with id ${block.id} not found")
        }

        blocks[indexOfUpdatedBlock] = block
    }

    override suspend fun deleteBlock(block: Block) {
        TODO("Not yet implemented")
    }

    override fun getAllBlocksWithSessions(): Flow<List<BlockWithSessions<Session>>> {
        return flow { emit(
            blocks.map { block ->
                BlockWithSessions(
                    id = block.id,
                    name = block.name,
                    sessions = days.filter { it.blockId == block.id }.map { it.id }.let { blockDayIds ->
                        sessions.filter { it.dayId in blockDayIds }
                    }
                )}
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

    override fun getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockId(
        blockId: Long
    ): Flow<List<DayWithSessions<SessionWithExercises<ExerciseWithMovementAndSets>>>> {
        return flow { emit(
            blocks.find { it.id == blockId }?.let { block ->
                days.filter { it.blockId == block.id }.map { day ->
                    DayWithSessions(
                        id = day.id,
                        blockId = day.blockId,
                        order = day.order,
                        sessions = sessions.filter { it.dayId == day.id }.map { session ->
                            SessionWithExercises(
                                id = session.id,
                                date = session.date,
                                dayId = session.dayId,
                                exercises = exercises.filter { it.sessionId == session.id }.map { exercise ->
                                    ExerciseWithMovementAndSets(
                                        id = exercise.id,
                                        sessionId = exercise.sessionId,
                                        movementId = exercise.movementId,
                                        order = exercise.order,
                                        supersetOrder = exercise.supersetOrder,
                                        movement = movements.find {
                                            it.id == exercise.movementId
                                        } ?: throw Exception("Movement with id ${exercise.movementId} not found"),
                                        sets = sets.filter { it.exerciseId == exercise.id }
                                    )
                                }
                            )
                        }
                    )
                }
            } ?: throw Exception("Block with id $blockId not found")
        )}
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
        val sessionToBeInserted = if (session.id == 0L) {
            session.copy(id = (sessions.size + 1).toLong())
        } else session

        sessions.add(sessionToBeInserted)
        sessions.shuffle()

        exercises.forEach { exercise ->
            val exerciseToBeInserted = exercise.copy(
                id = if (exercise.id == 0L) {
                    (this.exercises.size + 1).toLong()
                } else exercise.id,
                sessionId = sessionToBeInserted.id
            )
            this.exercises.add(exerciseToBeInserted)
        }
        this.exercises.shuffle()

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
        exercises.groupBy { it.sessionId }.forEach { (sessionId, exercises) ->
            val exercisesOrder = this.exercises.filter { it.sessionId == sessionId }.size + 1

            exercises.forEachIndexed { index, exercise ->
                val exerciseToBeInserted = exercise.copy(
                    id = if (exercise.id == 0L) {
                        (this.exercises.size + 1).toLong()
                    } else exercise.id,
                    order = exercisesOrder,
                    supersetOrder = index + 1
                )
                this.exercises.add(exerciseToBeInserted)
            }
        }
        this.exercises.shuffle()
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
        val setToBeInserted = set.copy(
            id = if (set.id == 0L) {
                (sets.size + 1).toLong()
            } else set.id,
            order = sets.filter { it.exerciseId == set.exerciseId }.size + 1
        )

        sets.add(setToBeInserted)
        sets.shuffle()

        return setToBeInserted.id
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
        val movementToBeInserted = if (movement.id == 0L) {
            movement.copy(id = (movements.size+1).toLong())
        } else movement

        movements.add(movementToBeInserted)
        movements.shuffle()

        return movementToBeInserted.id
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