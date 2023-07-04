package com.example.scarlet.feature_training_log.data.data_source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.scarlet.feature_training_log.domain.model.Set
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDao {

    @Query("SELECT * FROM 'set' WHERE exerciseId = :exerciseId")
    fun getSetsByExerciseId(exerciseId: Int): Flow<List<Set>>

    @Upsert
    suspend fun upsertSet(set: Set)

}
