package com.example.scarlet.feature_training_log.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.scarlet.feature_training_log.data.data_source.entity.ExerciseEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.ExerciseWithMovementAndSetsEntity

@Dao
interface ExerciseDao {

    @Transaction
    @Query("SELECT * FROM exercise WHERE exercise.sessionId = :sessionId")
    suspend fun getExercisesWithMovementAndSetsBySessionId(sessionId: Long):
            List<ExerciseWithMovementAndSetsEntity>

    @Upsert
    suspend fun insertExercise(exercise: ExerciseEntity): Long

    @Update
    suspend fun updateExercise(exercise: ExerciseEntity)

    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntity)

}
