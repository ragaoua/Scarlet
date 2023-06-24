package com.example.scarlet.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.scarlet.db.model.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise WHERE id = :exerciseId")
    fun getExerciseById(exerciseId: Int): Flow<Exercise?>

    @Query("SELECT * FROM exercise WHERE sessionId = :sessionId")
    fun getExercisesBySessionId(sessionId: Int): Flow<List<Exercise>>

}
