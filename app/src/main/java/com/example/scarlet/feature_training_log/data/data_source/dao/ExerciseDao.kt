package com.example.scarlet.feature_training_log.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.scarlet.feature_training_log.data.data_source.entity.ExerciseEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.ExerciseWithMovementAndSetsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Transaction
    @Query("""
        SELECT
            exercise.id AS exercise_id, 
            sessionId AS exercise_sessionId,
            exercise.movementId AS exercise_movementId,
            "order" AS exercise_order,
            movement.id AS movement_id,
            name AS movement_name
        FROM exercise
        INNER JOIN movement ON exercise.movementId = movement.id
        WHERE exercise.sessionId = :sessionId
    """)
    fun getExercisesWithMovementAndSetsBySessionId(sessionId: Int):
            Flow<List<ExerciseWithMovementAndSetsEntity>>

    @Upsert
    suspend fun insertExercise(exercise: ExerciseEntity): Long

    @Update
    suspend fun updateExercise(exercise: ExerciseEntity)

    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntity)

}
