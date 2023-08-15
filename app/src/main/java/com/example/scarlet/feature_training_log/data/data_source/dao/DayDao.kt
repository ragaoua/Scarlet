package com.example.scarlet.feature_training_log.data.data_source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scarlet.feature_training_log.data.data_source.entity.DayEntity

@Dao
interface DayDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDay(day: DayEntity): Long

    @Query("SELECT * FROM day WHERE blockId = :blockId")
    suspend fun getDaysByBlockId(blockId: Long): List<DayEntity>

}
