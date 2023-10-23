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

    override suspend fun insertBlockWithDaysWithSessionsWithExercisesWithMovementAndSets(
        block: Block,
        days: List<Day>,
        sessions: List<Session>,
        exercises: List<Exercise>,
        movements: List<Movement>,
        sets: List<Set>
    ): Long =
        dbInstance.blockDao.insertBlockWithDaysWithSessionsWithExercisesWithMovementAndSets(
            block = BlockEntity(block),
            days = days.map { DayEntity(it) },
            sessions = sessions.map { SessionEntity(it) },
            exercises = exercises.map { ExerciseEntity(it) },
            movements = movements.map { MovementEntity(it) },
            sets = sets.map { SetEntity(it) },
            dayDao = dbInstance.dayDao,
            sessionDao = dbInstance.sessionDao,
            exerciseDao = dbInstance.exerciseDao,
            movementDao = dbInstance.movementDao,
            setDao = dbInstance.setDao
        )

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

    override suspend fun insertSessionWithExercisesWithMovementAndSets(
        session: Session,
        exercises: List<Exercise>,
        movements: List<Movement>,
        sets: List<Set>
    ): Long =
        dbInstance.sessionDao.insertSessionWithExercisesWithMovementAndSets(
            session = SessionEntity(session),
            exercises = exercises.map { ExerciseEntity(it) },
            movements = movements.map { MovementEntity(it) },
            sets = sets.map { SetEntity(it) },
            exerciseDao = dbInstance.exerciseDao,
            movementDao = dbInstance.movementDao,
            setDao = dbInstance.setDao
        )

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// EXERCISE ///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun insertExercisesWhileSettingOrder(exercises: List<Exercise>) {
        dbInstance.exerciseDao.insertExercisesWhileSettingOrder(
            exercises.map { ExerciseEntity(it) }
        )
    }

    override suspend fun updateExercise(exercise: Exercise) {
        dbInstance.exerciseDao.updateExercise(ExerciseEntity(exercise))
    }

    override suspend fun deleteExerciseAndUpdateSubsequenceExercisesOrder(exercise: Exercise) =
        dbInstance.exerciseDao.deleteExerciseAndUpdateSubsequentExercisesOrder(ExerciseEntity(exercise))

    override suspend fun getExercisesWithMovementAndSetsBySessionId(sessionId: Long) =
        dbInstance.exerciseDao.getExercisesWithMovementAndSetsBySessionId(sessionId)
            .map { entity ->
                entity.toModel()
            }

    override suspend fun insertExerciseWithMovementAndSets(
        exercises: Exercise,
        movements: Movement,
        sets: List<Set>
    ) {
        dbInstance.exerciseDao.insertExerciseWithMovementAndSets(
            ExerciseEntity(exercises),
            MovementEntity(movements),
            sets.map { SetEntity(it) },
            dbInstance.movementDao,
            dbInstance.setDao
        )
    }

    override suspend fun insertSupersetExerciseWithMovementAndSets(
        exercises: Exercise,
        movements: Movement,
        sets: List<Set>
    ) {
        dbInstance.exerciseDao.insertSupersetExerciseWithMovementAndSets(
            exercise = ExerciseEntity(exercises),
            movement = MovementEntity(movements),
            sets = sets.map { SetEntity(it) },
            movementDao = dbInstance.movementDao,
            setDao = dbInstance.setDao
        )
    }

    override suspend fun getNbExercisesByMovementId(movementId: Long): Int =
        dbInstance.exerciseDao.getNbExercisesByMovementId(movementId)

    override suspend fun updateExerciseOrder(exercises: List<Exercise>, newOrder: Int) {
        dbInstance.exerciseDao.updateExercisesOrder(
            exercises = exercises.map { ExerciseEntity(it) },
            newOrder = newOrder
        )
    }

    override suspend fun updateExerciseSupersetOrder(exercise: Exercise, newSupersetOrder: Int) {
        dbInstance.exerciseDao.updateExerciseSupersetOrder(
            exercise = ExerciseEntity(exercise),
            newSupersetOrder = newSupersetOrder
        )
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////// SET /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun insertSetWhileSettingOrder(set: Set) =
        dbInstance.setDao.insertSetWhileSettingOrder(SetEntity(set))

    override suspend fun updateSet(set: Set) = dbInstance.setDao.updateSet(SetEntity(set))

    override suspend fun deleteSetAndUpdateSubsequentSetsOrder(set: Set) =
        dbInstance.setDao.deleteSetAndUpdateSubsequentSetsOrder(SetEntity(set))

    override suspend fun restoreSet(set: Set) {
        dbInstance.setDao.restoreSet(SetEntity(set))
    }

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