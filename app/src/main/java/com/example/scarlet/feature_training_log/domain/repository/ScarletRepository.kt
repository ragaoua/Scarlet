package com.example.scarlet.feature_training_log.domain.repository

import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithList
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.DayWithSessionsWithExercisesWithMovement
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.Set
import kotlinx.coroutines.flow.Flow

interface ScarletRepository {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// BLOCK ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun insertBlock(block: Block): Long
    suspend fun updateBlock(block: Block)
    suspend fun deleteBlock(block: Block)

    fun getAllBlocksWithSessions(): Flow<List<BlockWithList<DayWithSessions>>>
    suspend fun getBlockByName(name: String): Block?

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////// DAY /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun insertDay(day: Day): Long
    suspend fun getDaysByBlockId(blockId: Long): List<Day>

    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// SESSION ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun insertSession(session: Session): Long
    suspend fun updateSession(session: Session)
    suspend fun deleteSession(session: Session)

    fun getDaysWithSessionsWithExercisesWithMovementByBlockId(blockId: Long):
            Flow<List<DayWithSessionsWithExercisesWithMovement>>

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

    fun getAllMovements(): Flow<List<Movement>>

}