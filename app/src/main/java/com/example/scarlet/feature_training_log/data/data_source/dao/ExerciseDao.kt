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

    @Transaction
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

    @Query("""
        SELECT COUNT(*)
        FROM exercise
        WHERE exercise.movementId = :movementId
    """)
    suspend fun getNbExercisesByMovementId(movementId: Long): Int

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

    @Query(
        """
        SELECT *
        FROM exercise
        WHERE sessionId = :sessionId
        AND `order` BETWEEN :lowerEndOrder AND :upperEndOrder
    """
    )
    suspend fun getExercisesBySessionIdWhereOrderIsBetween(
        sessionId: Long,
        lowerEndOrder: Int,
        upperEndOrder: Int
    ): List<ExerciseEntity>

    /**
     * Update the order of a list of exercises and update
     * the order of the other exercises if necessary
     *
     * Assume the exercises share the same session id and order.
     * Otherwise, an exception can be thrown as the unique
     * constraint on (sessionId,order,supersetOrder) can be violated
     *
     * @param exercises list of exercises to update
     * @param newOrder new order of the exercises
     */
    @Transaction
    suspend fun updateExercisesOrder(exercises: List<ExerciseEntity>, newOrder: Int) {
        exercises.forEach { exercise ->
            // 1 - Temporarily set the order of the exercise to 0 to avoid unique
            // constraint violation on (sessionId,order,supersetOrder).
            updateExercise(exercise.copy(order = 0))
        }

        exercises.forEach { exercise ->
            // 2 - Update other exercises orders
            if (newOrder < exercise.order) {
                getExercisesBySessionIdWhereOrderIsBetween(
                    sessionId = exercise.sessionId,
                    lowerEndOrder = newOrder,
                    upperEndOrder = exercise.order - 1
                ).sortedByDescending { it.order } /* Sorting is necessary to avoid unique constraint
                                                     violation on (sessionId,order,supersetOrder) when
                                                     updating */
                    .forEach {
                        updateExercise(it.copy(order = it.order + 1))
                    }
            } else {
                getExercisesBySessionIdWhereOrderIsBetween(
                    sessionId = exercise.sessionId,
                    lowerEndOrder = exercise.order + 1,
                    upperEndOrder = newOrder
                ).sortedBy { it.order } /* Sorting is necessary to avoid unique constraint
                                           violation on (sessionId,order,supersetOrder) when
                                           updating */
                    .forEach {
                        updateExercise(it.copy(order = it.order - 1))
                    }
            }
        }

        exercises.forEach { exercise ->
            // 3 - Finally update the exercise's order
            updateExercise(exercise.copy(order = newOrder))
        }
    }

    @Query(
        """
        SELECT *
        FROM exercise
        WHERE sessionId = :sessionId
        AND `order` = :exerciseOrder
        AND supersetOrder BETWEEN :lowerEndSupersetOrder AND :upperEndSupersetOrder
    """
    )
    suspend fun getExercisesBySessionIdAndOrderWhereSupersetOrderIsBetween(
        sessionId: Long,
        exerciseOrder: Int,
        lowerEndSupersetOrder: Int,
        upperEndSupersetOrder: Int
    ): List<ExerciseEntity>

    /**
     * Update the supersetOrder of an exercise and update
     * the supersetOrder of the other exercises if necessary
     *
     * @param exercise exercise to update
     * @param newSupersetOrder new supersetOrder of the exercise
     */
    @Transaction
    suspend fun updateExerciseSupersetOrder(exercise: ExerciseEntity, newSupersetOrder: Int) {
        // 1 - Temporarily set the superOrder of the exercise to 0 to avoid unique
        // constraint violation on (sessionId,order,supersetOrder).
        updateExercise(exercise.copy(supersetOrder = 0))

        // 2 - Update other superset exercises supersetOrders
        if (newSupersetOrder < exercise.supersetOrder) {
            getExercisesBySessionIdAndOrderWhereSupersetOrderIsBetween(
                sessionId = exercise.sessionId,
                exerciseOrder = exercise.order,
                lowerEndSupersetOrder = newSupersetOrder,
                upperEndSupersetOrder = exercise.supersetOrder - 1
            ).sortedByDescending { it.supersetOrder } /* Sorting is necessary to avoid unique
                                                         constraint violation on
                                                         (sessionId,order,supersetOrder) when
                                                         updating */
                .forEach {
                    updateExercise(it.copy(supersetOrder = it.supersetOrder + 1))
                }
        } else {
            getExercisesBySessionIdAndOrderWhereSupersetOrderIsBetween(
                sessionId = exercise.sessionId,
                exerciseOrder = exercise.order,
                lowerEndSupersetOrder = exercise.supersetOrder + 1,
                upperEndSupersetOrder = newSupersetOrder
            ).sortedBy { it.supersetOrder } /* Sorting is necessary to avoid unique constraint
                                       violation on (sessionId,order,supersetOrder) when
                                       updating */
                .forEach {
                    updateExercise(it.copy(supersetOrder = it.supersetOrder - 1))
                }
        }

        // 3 - Finally update the exercise's order
        updateExercise(exercise.copy(supersetOrder = newSupersetOrder))
    }




}
