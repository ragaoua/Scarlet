package com.example.scarlet.feature_training_log.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.scarlet.feature_training_log.data.data_source.entity.MovementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovementDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMovement(movement: MovementEntity): Long

    @Update
    suspend fun updateMovement(movement: MovementEntity)

    @Delete
    suspend fun deleteMovement(movement: MovementEntity)

    @Query("SELECT * FROM movement")
    fun getAllMovements(): Flow<List<MovementEntity>>

    @Query("SELECT * FROM movement WHERE name = :name")
    suspend fun getMovementByName(name: String): MovementEntity?
}
