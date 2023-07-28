package com.example.scarlet.feature_training_log.domain.repository

import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercisesWithMovementName
import com.example.scarlet.feature_training_log.domain.model.Set
import kotlinx.coroutines.flow.Flow

interface ScarletRepository {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// BLOCK ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    suspend fun insertBlock(block: Block): Long

    suspend fun updateBlock(block: Block)

    suspend fun deleteBlock(block: Block)

    fun getBlocksWithSessionsByCompleted(completed: Boolean): Flow<List<BlockWithSessions>>

    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// SESSION ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    suspend fun insertSession(session: Session): Long

    suspend fun updateSession(session: Session)

    suspend fun deleteSession(session: Session)

    fun getSessionsWithExercisesWithMovementNameByBlockId(blockId: Int):
            Flow<List<SessionWithExercisesWithMovementName>>

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// EXERCISE ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    suspend fun insertExercise(exercise: Exercise): Long

    fun getExercisesWithMovementAndSetsBySessionId(sessionId: Int):
            Flow<List<ExerciseWithMovementAndSets>>

    suspend fun deleteExercise(exercise: Exercise)

    suspend fun updateExercise(exercise: Exercise)

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