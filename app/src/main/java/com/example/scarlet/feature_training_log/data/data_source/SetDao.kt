package com.example.scarlet.feature_training_log.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.scarlet.feature_training_log.domain.model.Set
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDao {

    @Query("SELECT * FROM 'set' WHERE exerciseId = :exerciseId")
    fun getSetsByExerciseId(exerciseId: Int): Flow<List<Set>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSet(set: Set)

    @Update
    suspend fun updateSet(set: Set)

    @Delete
    suspend fun deleteSet(set: Set)

}
