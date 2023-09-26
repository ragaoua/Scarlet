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

    suspend fun insertMovement(
        movement: MovementEntity,
        onConflict: Int = OnConflictStrategy.ABORT
    ): Long {
        return when(onConflict) {
            OnConflictStrategy.ABORT -> insertMovementOnConflictAbort(movement)
            OnConflictStrategy.IGNORE -> insertMovementOnConflictIgnore(movement)
            else -> throw IllegalArgumentException("Invalid onConflictStrategy")
        }
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMovementOnConflictAbort(movement: MovementEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovementOnConflictIgnore(movement: MovementEntity): Long

    @Update
    suspend fun updateMovement(movement: MovementEntity)

    @Delete
    suspend fun deleteMovement(movement: MovementEntity)

    @Query("SELECT * FROM movement")
    fun getAllMovements(): Flow<List<MovementEntity>>

    @Query("SELECT * FROM movement WHERE name = :name")
    suspend fun getMovementByName(name: String): MovementEntity?
}
