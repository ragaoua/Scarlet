package com.example.scarlet.feature_training_log.data.data_source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
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
            Flow<List<ExerciseWithMovementAndSets>>

    @Upsert
    suspend fun insertExercise(exercise: Exercise)

}
