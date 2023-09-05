package com.example.scarlet.feature_training_log.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.scarlet.feature_training_log.data.data_source.entity.BlockEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.BlockWithDaysWithSessionsEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.DayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertBlock(block: BlockEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDay(day: DayEntity): Long

    @Transaction
    suspend fun insertBlockWithDays(block: BlockEntity, days: List<DayEntity>): Long {
        val blockId = insertBlock(block)

        days.forEach { day ->
            insertDay(day.copy(
                blockId = blockId
            ))
        }

        return blockId
    }

    @Update
    suspend fun updateBlock(block: BlockEntity)

    @Delete
    suspend fun deleteBlock(block: BlockEntity)

    @Transaction
    @Query("SELECT * FROM block")
    fun getAllBlocks(): Flow<List<BlockWithDaysWithSessionsEntity>>

    @Query("""
        SELECT *
        FROM block
        WHERE NAME = :name
    """)
    suspend fun getBlockByName(name: String): BlockEntity?
}
