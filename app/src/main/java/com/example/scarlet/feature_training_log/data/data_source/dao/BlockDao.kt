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
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertBlock(block: BlockEntity): Long

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
