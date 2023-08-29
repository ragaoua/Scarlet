package com.example.scarlet.feature_training_log.data.data_source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.scarlet.feature_training_log.data.data_source.entity.DayEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.DayWithSessionsWithExercisesWithMovementAndSetsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DayDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDay(day: DayEntity): Long

    @Query("SELECT * FROM day WHERE blockId = :blockId")
    suspend fun getDaysByBlockId(blockId: Long): List<DayEntity>

    @Transaction
    @Query("SELECT * FROM day WHERE day.blockId = :blockId")
    fun getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockIdTest(blockId: Long):
            Flow<List<DayWithSessionsWithExercisesWithMovementAndSetsEntity>>

}
