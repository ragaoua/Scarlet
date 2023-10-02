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

    @Transaction
    suspend fun deleteExerciseAndUpdateSubsequentSetsOrder(exercise: ExerciseEntity) {
        deleteExercise(exercise)

        getExercisesBySessionIdWhereOrderIsGreaterThan(
            sessionId = exercise.sessionId,
            exerciseOrder = exercise.order
        ).forEach {
            updateExercise(it.copy(order = it.order - 1))
        }
    }

    @Query(
        """
        SELECT *
        FROM exercise
        WHERE sessionId = :sessionId
        AND `order` > :exerciseOrder
    """)
    suspend fun getExercisesBySessionIdWhereOrderIsGreaterThan(sessionId: Long, exerciseOrder: Int):
            List<ExerciseEntity>

    @Transaction
    suspend fun insertExerciseWithMovementAndSets(
        exercise: ExerciseEntity,
        movement: MovementEntity,
        sets: List<SetEntity>,
        movementDao: MovementDao,
        setDao: SetDao
    ) {
        movementDao.insertMovement(movement, onConflict = OnConflictStrategy.IGNORE)
        getExercisesBySessionIdWhereOrderIsGreaterThan(
            sessionId = exercise.sessionId,
            exerciseOrder = exercise.order - 1
        ).sortedByDescending { it.order } /* Sorting is necessary to satisfy the constraint
                                             that (sessionId,order) must be unique */
            .forEach {
                updateExercise(it.copy(order = it.order + 1))
            }
        insertExercise(exercise)
        sets.forEach { setDao.insertSet(it) }
    }

    @Transaction
    suspend fun insertExerciseWhileSettingOrder(exercise: ExerciseEntity): Long {
        val order = getExercisesBySessionId(exercise.sessionId).size + 1
        return insertExercise(exercise.copy(order = order))
    }

}
