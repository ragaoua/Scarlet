package com.example.scarlet.feature_training_log.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertBlock(block: Block): Long

    @Update
    suspend fun updateBlock(block: Block)

    @Delete
    suspend fun deleteBlock(block: Block)

    @Transaction
    @Query("""
        SELECT *
        FROM block
        WHERE COMPLETED = :completed
    """)
    fun getBlocksWithSessionsByCompleted(completed: Boolean): Flow<List<BlockWithSessions>>
}
