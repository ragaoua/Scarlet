package com.example.scarlet.feature_training_log.data.repository

import com.example.scarlet.feature_training_log.data.data_source.ScarletDatabase
import com.example.scarlet.feature_training_log.data.data_source.entity.BlockEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.DayEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.ExerciseEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.MovementEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.SessionEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.SetEntity
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.map

class ScarletRepositoryImpl(
    private val dbInstance: ScarletDatabase
) : ScarletRepository {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// BLOCK ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun insertBlock(block: Block) =
        dbInstance.blockDao.insertBlock(BlockEntity(block))

    override suspend fun updateBlock(block: Block) =
        dbInstance.blockDao.updateBlock(BlockEntity(block))

    override suspend fun deleteBlock(block: Block) =
        dbInstance.blockDao.deleteBlock(BlockEntity(block))

    override fun getAllBlocksWithSessions() =
        dbInstance.blockDao.getAllBlocksWithSessions()
            .map { entityList ->
                entityList.map { entity ->
                    entity.toModel()
                }
            }

    override suspend fun getBlockByName(name: String): Block? =
        dbInstance.blockDao.getBlockByName(name)?.toBlock()

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////// DAY /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun insertDay(day: Day): Long =
        dbInstance.dayDao.insertDay(DayEntity(day))

    override suspend fun getDaysByBlockId(blockId: Long): List<Day> =
        dbInstance.dayDao.getDaysByBlockId(blockId)
            .map { it.toDay() }

    override fun getDaysWithSessionsWithExercisesWithMovementByBlockId(blockId: Long) =
        dbInstance.dayDao.getDaysWithSessionsWithMovementByBlockId(blockId)
            .map { entityList ->
                entityList.map { entity ->
                    entity.toDayWithSessionsWithExercisesWithMovement()
                }
            }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// SESSION ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun insertSession(session: Session) =
        dbInstance.sessionDao.insertSession(SessionEntity(session))

    override suspend fun deleteSession(session: Session) =
        dbInstance.sessionDao.deleteSession(SessionEntity(session))

    override suspend fun updateSession(session: Session) =
        dbInstance.sessionDao.updateSession(SessionEntity(session))

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// EXERCISE ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun insertExercise(exercise: Exercise) =
        dbInstance.exerciseDao.insertExercise(ExerciseEntity(exercise))

    override suspend fun updateExercise(exercise: Exercise) {
        dbInstance.exerciseDao.updateExercise(ExerciseEntity(exercise))
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        dbInstance.exerciseDao.deleteExercise(ExerciseEntity(exercise))
    }

    override fun getExercisesWithMovementAndSetsBySessionId(sessionId: Long) =
        dbInstance.exerciseDao.getExercisesWithMovementAndSetsBySessionId(sessionId)
            .map { entityList ->
                entityList.map { entity ->
                    entity.toExerciseWithMovementAndSets()
                }
            }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////// SET /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun insertSet(set: Set) = dbInstance.setDao.insertSet(SetEntity(set))

    override suspend fun updateSet(set: Set) = dbInstance.setDao.updateSet(SetEntity(set))

    override suspend fun deleteSet(set: Set) = dbInstance.setDao.deleteSet(SetEntity(set))


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// MOVEMENT ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun insertMovement(movement: Movement) =
        dbInstance.movementDao.insertMovement(MovementEntity(movement))

    override fun getAllMovements() =
        dbInstance.movementDao.getAllMovements()
            .map { entityList ->
                entityList.map { entity ->
                    entity.toMovement()
                }
            }
}