package com.example.scarlet.feature_training_log.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.scarlet.feature_training_log.data.data_source.entity.ExerciseEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.ExerciseWithMovementAndSetsEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.MovementEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.SetEntity

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise WHERE exercise.sessionId = :sessionId")
    suspend fun getExercisesWithMovementAndSetsBySessionId(sessionId: Long):
            List<ExerciseWithMovementAndSetsEntity>

    @Query("SELECT * FROM exercise WHERE exercise.sessionId = :sessionId")
    suspend fun getExercisesBySessionId(sessionId: Long):
            List<ExerciseEntity>

    @Upsert
    suspend fun insertExercise(exercise: ExerciseEntity): Long

    @Update
    suspend fun updateExercise(exercise: ExerciseEntity)

    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntity)

    @Query(
        """
        SELECT *
        FROM exercise
        WHERE sessionId = :sessionId
        AND `order` > :exerciseOrder
    """)
    suspend fun getExercisesBySessionIdWhereOrderIsGreaterThan(sessionId: Long, exerciseOrder: Int):
            List<ExerciseEntity>

    @Query(
        """
        SELECT *
        FROM exercise
        WHERE sessionId = :sessionId
        AND `order` = :exerciseOrder
        AND supersetOrder > :supersetOrder
    """)
    suspend fun getExercisesBySessionIdAndOrderWhereSupersetOrderIsGreaterThan(
        sessionId: Long,
        exerciseOrder: Int,
        supersetOrder: Int
    ): List<ExerciseEntity>

    @Transaction
    suspend fun deleteExerciseAndUpdateSubsequentExercisesOrder(exercise: ExerciseEntity) {
        deleteExercise(exercise)

        val supersetExercises = getExercisesBySessionIdAndOrderWhereSupersetOrderIsGreaterThan(
            sessionId = exercise.sessionId,
            exerciseOrder = exercise.order,
            supersetOrder = 0
        )
        supersetExercises
            .filter { it.supersetOrder > exercise.supersetOrder }
            .sortedBy { it.supersetOrder } /* Sorting is necessary to satisfy the unique
                                              constraint on (sessionId,order,supersetOrder) */
            .forEach {
                updateExercise(it.copy(supersetOrder = it.supersetOrder - 1))
            }
        if(supersetExercises.isEmpty()) {
            val subsequentExercises = getExercisesBySessionIdWhereOrderIsGreaterThan(
                sessionId = exercise.sessionId,
                exerciseOrder = exercise.order
            )
            subsequentExercises
                .sortedBy { it.order } /* Sorting is necessary to satisfy the unique
                                      constraint on (sessionId,order,supersetOrder) */
                .forEach {
                    updateExercise(it.copy(order = it.order - 1))
                }
        }
    }

    @Transaction
    suspend fun insertExerciseWithMovementAndSets(
        exercise: ExerciseEntity,
        movement: MovementEntity,
        sets: List<SetEntity>,
        movementDao: MovementDao,
        setDao: SetDao
    ) {
        movementDao.insertMovement(movement, onConflict = OnConflictStrategy.IGNORE)

        val subsequentExercises = getExercisesBySessionIdWhereOrderIsGreaterThan(
            sessionId = exercise.sessionId,
            exerciseOrder = exercise.order - 1
        )
        subsequentExercises
            .sortedByDescending { it.order } /* Sorting is necessary to satisfy the unique
                                                constraint on (sessionId,order,supersetOrder) */
            .forEach {
                updateExercise(it.copy(order = it.order + 1))
            }

        insertExercise(exercise)
        sets.forEach { setDao.insertSet(it) }
    }

    @Transaction
    suspend fun insertSupersetExerciseWithMovementAndSets(
        exercise: ExerciseEntity,
        movement: MovementEntity,
        sets: List<SetEntity>,
        movementDao: MovementDao,
        setDao: SetDao
    ) {
        movementDao.insertMovement(movement, onConflict = OnConflictStrategy.IGNORE)

        val subsequentSupersetExercises =
            getExercisesBySessionIdAndOrderWhereSupersetOrderIsGreaterThan(
                sessionId = exercise.sessionId,
                exerciseOrder = exercise.order,
                supersetOrder = exercise.supersetOrder - 1
            )
        subsequentSupersetExercises
            .sortedByDescending { it.supersetOrder } /* Sorting is necessary to satisfy
                                                        the unique constraint on
                                                        (sessionId,order,supersetOrder) */
            .forEach {
                updateExercise(it.copy(supersetOrder = it.supersetOrder + 1))
            }

        insertExercise(exercise)
        sets.forEach { setDao.insertSet(it) }
    }

    @Transaction
    suspend fun insertExercisesWhileSettingOrder(exercises: List<ExerciseEntity>) {
        exercises.groupBy { it.sessionId }.forEach { (sessionId, exercises) ->
            val order = (getExercisesBySessionId(sessionId).maxOfOrNull { it.order } ?: 0) + 1
            exercises.forEachIndexed { index, exercise ->
                insertExercise(exercise.copy(
                    order = order,
                    supersetOrder = index + 1
                ))
            }
        }
    }

}
