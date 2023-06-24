package com.example.scarlet.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.scarlet.db.model.Set
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDao {

    @Query("SELECT * FROM 'set' WHERE exerciseId = :exerciseId")
    fun getSetsByExerciseId(exerciseId: Int): Flow<List<Set>>

}
