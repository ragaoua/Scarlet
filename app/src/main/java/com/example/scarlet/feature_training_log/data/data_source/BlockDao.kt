package com.example.scarlet.feature_training_log.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithDates
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertBlock(block: Block): Long

    @Update
    suspend fun updateBlock(block: Block)

    @Delete
    suspend fun deleteBlock(block: Block)

    @Query("""
        SELECT block.*, MIN(session.date) AS firstSessionDate, MAX(session.date) AS lastSessionDate
        FROM block
        LEFT JOIN session ON block.id = session.blockId
        WHERE COMPLETED = :completed
        GROUP BY block.id, block.name, block.completed
    """)
    fun getBlocksWithDatesByCompleted(completed: Boolean): Flow<List<BlockWithDates>>
}
