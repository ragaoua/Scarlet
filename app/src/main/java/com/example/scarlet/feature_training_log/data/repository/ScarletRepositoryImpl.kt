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
    override suspend fun insertBlockWithDays(block: Block, days: List<Day>) =
        dbInstance.blockDao.insertBlockWithDays(
            BlockEntity(block),
            days.map { DayEntity(it) },
            dbInstance.dayDao
        )

    override suspend fun updateBlock(block: Block) =
        dbInstance.blockDao.updateBlock(BlockEntity(block))

    override suspend fun deleteBlock(block: Block) =
        dbInstance.blockDao.deleteBlock(BlockEntity(block))

    override fun getAllBlocks() =
        dbInstance.blockDao.getAllBlocks()
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
    override suspend fun getDaysByBlockId(blockId: Long): List<Day> =
        dbInstance.dayDao.getDaysByBlockId(blockId)
            .map { it.toDay() }

    override fun getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockId(blockId: Long) =
        dbInstance.dayDao.getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockIdTest(blockId)
            .map { days ->
                days.map { day ->
                    day.toModel()
                }
            }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// SESSION ///////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun deleteSession(session: Session) =
        dbInstance.sessionDao.deleteSession(SessionEntity(session))

    override suspend fun updateSession(session: Session) =
        dbInstance.sessionDao.updateSession(SessionEntity(session))

    override suspend fun getSessionsWithExercisesByDayId(dayId: Long) =
        dbInstance.sessionDao.getSessionsWithExercisesByDayId(dayId)
            .map { entity ->
                entity.toModel()
            }

    override suspend fun insertSessionWithExercises(session: Session, exercises: List<Exercise>) =
        dbInstance.sessionDao.insertSessionWithExercises(
            SessionEntity(session),
            exercises.map { ExerciseEntity(it) },
            dbInstance.exerciseDao
        )

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
                    entity.toModel()
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

    override suspend fun updateMovement(movement: Movement) =
        dbInstance.movementDao.updateMovement(MovementEntity(movement))

    override suspend fun deleteMovement(movement: Movement) =
        dbInstance.movementDao.deleteMovement(MovementEntity(movement))

    override fun getAllMovements() =
        dbInstance.movementDao.getAllMovements()
            .map { entityList ->
                entityList.map { entity ->
                    entity.toMovement()
                }
            }

    override suspend fun getMovementByName(name: String): Movement? =
        dbInstance.movementDao.getMovementByName(name)?.toMovement()
}