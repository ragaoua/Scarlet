package com.example.scarlet.feature_training_log.data.repository

import com.example.scarlet.feature_training_log.data.data_source.ScarletDatabase
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class ScarletRepositoryImpl(
    private val dbInstance: ScarletDatabase
) : ScarletRepository {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// BLOCK ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    override fun getBlocksWithSessionsByCompleted(completed: Boolean) =
        dbInstance.blockDao.getBlocksWithSessionsByCompleted(completed)

    override suspend fun insertBlock(block: Block) = dbInstance.blockDao.insertBlock(block)

    override suspend fun updateBlock(block: Block) = dbInstance.blockDao.updateBlock(block)

    override suspend fun deleteBlock(block: Block) = dbInstance.blockDao.deleteBlock(block)


    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// SESSION ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    override fun getSessionsWithMovementsByBlockId(blockId: Int) =
        dbInstance.sessionDao.getSessionsWithMovementsByBlockId(blockId)

    override suspend fun insertSession(session: Session) =
        dbInstance.sessionDao.insertSession(session)

    override suspend fun deleteSession(session: Session) =
        dbInstance.sessionDao.deleteSession(session)

    override suspend fun updateSession(session: Session) =
        dbInstance.sessionDao.updateSession(session)


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// EXERCISE ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun insertExercise(exercise: Exercise) =
        dbInstance.exerciseDao.insertExercise(exercise)

    override fun getExercisesWithMovementAndSetsBySessionId(sessionId: Int) =
        dbInstance.exerciseDao.getExercisesWithMovementAndSetsBySessionId(sessionId)


    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////// SET /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun insertSet(set: Set) = dbInstance.setDao.insertSet(set)

    override suspend fun updateSet(set: Set) = dbInstance.setDao.updateSet(set)

    override suspend fun deleteSet(set: Set) = dbInstance.setDao.deleteSet(set)


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// MOVEMENT ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun getAllMovements() = dbInstance.movementDao.getAllMovements()

}