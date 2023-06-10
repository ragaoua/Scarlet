package com.example.scarlet.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.scarlet.model.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise WHERE sessionId = :sessionId")
    fun getExercisesBySessionId(sessionId: Int): Flow<List<Exercise>>
}
