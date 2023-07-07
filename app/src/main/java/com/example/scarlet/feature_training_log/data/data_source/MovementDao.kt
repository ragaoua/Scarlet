package com.example.scarlet.feature_training_log.data.data_source

import androidx.room.Dao
import androidx.room.Query
import com.example.scarlet.feature_training_log.domain.model.Movement
import kotlinx.coroutines.flow.Flow

@Dao
interface MovementDao {

    @Query("SELECT * FROM movement")
    fun getAllMovements(): Flow<List<Movement>>

}
