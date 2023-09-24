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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// EXERCISE ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun insertExercise(exercise: Exercise): Long
    suspend fun updateExercise(exercise: Exercise)
    suspend fun deleteExercise(exercise: Exercise)

    fun getExercisesWithMovementAndSetsBySessionId(sessionId: Long):
            Flow<List<ExerciseWithMovementAndSets>>

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////// SET /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun insertSet(set: Set): Long
    suspend fun updateSet(set: Set)
    suspend fun deleteSet(set: Set)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// MOVEMENT ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun insertMovement(movement: Movement): Long
    suspend fun updateMovement(movement: Movement)
    suspend fun deleteMovement(movement: Movement)

    fun getAllMovements(): Flow<List<Movement>>
    suspend fun getMovementByName(name: String): Movement?

}