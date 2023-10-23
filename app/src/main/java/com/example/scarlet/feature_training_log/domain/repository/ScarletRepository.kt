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

interface ScarletRepository {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// BLOCK ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun insertBlockWithDays(block: Block, days: List<Day>): Long
    suspend fun updateBlock(block: Block)
    suspend fun deleteBlock(block: Block)

    fun getAllBlocks(): Flow<List<BlockWithDays<DayWithSessions<Session>>>>
    suspend fun getBlockByName(name: String): Block?

    suspend fun insertBlockWithDaysWithSessionsWithExercisesWithMovementAndSets(
        block: Block,
        days: List<Day>,
        sessions: List<Session>,
        exercises: List<Exercise>,
        movements: List<Movement>,
        sets: List<Set>
    ): Long

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////// DAY /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun getDaysByBlockId(blockId: Long): List<Day>

    fun getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockId(blockId: Long):
            Flow<List<DayWithSessions<SessionWithExercises<ExerciseWithMovementAndSets>>>>

    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// SESSION ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun updateSession(session: Session)
    suspend fun deleteSession(session: Session)

    suspend fun getSessionsWithExercisesByDayId(dayId: Long): List<SessionWithExercises<Exercise>>
    suspend fun insertSessionWithExercises(session: Session, exercises: List<Exercise>): Long
    suspend fun insertSessionWithExercisesWithMovementAndSets(
        session: Session,
        exercises: List<Exercise>,
        movements: List<Movement>,
        sets: List<Set>
    ): Long

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// EXERCISE ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun insertExercisesWhileSettingOrder(exercises: List<Exercise>)
    suspend fun updateExercise(exercise: Exercise)
    suspend fun deleteExerciseAndUpdateSubsequenceExercisesOrder(exercise: Exercise)

    suspend fun getExercisesWithMovementAndSetsBySessionId(sessionId: Long):
            List<ExerciseWithMovementAndSets>
    suspend fun insertExerciseWithMovementAndSets(
        exercises: Exercise,
        movements: Movement,
        sets: List<Set>
    )
    suspend fun insertSupersetExerciseWithMovementAndSets(
        exercises: Exercise,
        movements: Movement,
        sets: List<Set>
    )
    suspend fun getNbExercisesByMovementId(movementId: Long): Int

    /**
     * Update the order of a list of exercises and update
     * the order of the other exercises if necessary
     *
     * @param exercises list of exercises to update
     * @param newOrder new order of the exercises
     */
    suspend fun updateExerciseOrder(exercises: List<Exercise>, newOrder: Int)

    /**
     * Update the supersetOrder of an exercise and update
     * the supersetOrder of the other exercises if necessary
     *
     * @param exercise exercise to update
     * @param newSupersetOrder new supersetOrder of the exercise
     */
    suspend fun updateExerciseSupersetOrder(exercise: Exercise, newSupersetOrder: Int)

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////// SET /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun insertSetWhileSettingOrder(set: Set): Long
    suspend fun updateSet(set: Set)
    suspend fun deleteSetAndUpdateSubsequentSetsOrder(set: Set)

    suspend fun restoreSet(set: Set)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// MOVEMENT ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun insertMovement(movement: Movement): Long
    suspend fun updateMovement(movement: Movement)
    suspend fun deleteMovement(movement: Movement)

    fun getAllMovements(): Flow<List<Movement>>
    suspend fun getMovementByName(name: String): Movement?

}