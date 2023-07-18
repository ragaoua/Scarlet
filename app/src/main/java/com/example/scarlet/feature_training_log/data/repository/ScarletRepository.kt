package com.example.scarlet.feature_training_log.data.repository

import com.example.scarlet.feature_training_log.data.data_source.ScarletDatabase
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.Set

class ScarletRepository(
    private val dbInstance: ScarletDatabase
) {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// BLOCK ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    fun getBlocksWithSessionsByCompleted(completed: Boolean) =
        dbInstance.blockDao.getBlocksWithSessionsByCompleted(completed)

    suspend fun insertBlock(block: Block) = dbInstance.blockDao.insertBlock(block)

    suspend fun updateBlock(block: Block) = dbInstance.blockDao.updateBlock(block)

    suspend fun deleteBlock(block: Block) = dbInstance.blockDao.deleteBlock(block)

    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// SESSION ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    fun getSessionsWithMovementsByBlockId(blockId: Int) =
        dbInstance.sessionDao.getSessionsWithMovementsByBlockId(blockId)

    suspend fun insertSession(session: Session) = dbInstance.sessionDao.insertSession(session)

    suspend fun deleteSession(session: Session) = dbInstance.sessionDao.deleteSession(session)

    suspend fun updateSession(session: Session) = dbInstance.sessionDao.updateSession(session)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// EXERCISE ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    suspend fun insertExercise(exercise: Exercise) = dbInstance.exerciseDao.insertExercise(exercise)

    fun getExercisesWithMovementAndSetsBySessionId(sessionId: Int) =
        dbInstance.exerciseDao.getExercisesWithMovementAndSetsBySessionId(sessionId)

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////// SET /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    suspend fun insertSet(set: Set) = dbInstance.setDao.insertSet(set)

    suspend fun updateSet(set: Set) = dbInstance.setDao.updateSet(set)

    suspend fun deleteSet(set: Set) = dbInstance.setDao.deleteSet(set)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// MOVEMENT ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    fun getAllMovements() = dbInstance.movementDao.getAllMovements()

}