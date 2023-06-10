package com.example.scarlet.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.scarlet.model.Block
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockDao {

    @Query("SELECT * FROM block WHERE id = :blockId")
    fun getBlockById(blockId: Int): Flow<Block?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertBlock(block: Block): Long

    @Update
    suspend fun updateBlock(block: Block)

    @Delete
    suspend fun deleteBlock(block: Block)

    @Query("SELECT * FROM block WHERE COMPLETED = :completed")
    fun getBlocksByCompleted(completed: Boolean): Flow<List<Block>>
}
