package com.example.scarlet.feature_training_log.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.scarlet.feature_training_log.domain.model.Set

@Dao
interface SetDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSet(set: Set)

    @Update
    suspend fun updateSet(set: Set)

    @Delete
    suspend fun deleteSet(set: Set)

}
