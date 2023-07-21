package com.example.scarlet.feature_training_log.data.data_source.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.scarlet.feature_training_log.data.data_source.entity.MovementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovementDao {

    @Query("SELECT * FROM movement")
    fun getAllMovements(): Flow<List<MovementEntity>>

}
