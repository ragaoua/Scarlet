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
import com.example.scarlet.feature_training_log.data.data_source.entity.ExerciseEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.MovementEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.SessionEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.SetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertBlock(block: BlockEntity): Long

    @Transaction
    suspend fun insertBlockWithDays(
        block: BlockEntity,
        days: List<DayEntity>,
        dayDao: DayDao
    ): Long {
        val blockId = insertBlock(block)

        days.forEach { day ->
            dayDao.insertDay(day.copy(
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

    @Query("SELECT * FROM block WHERE name = :name")
    suspend fun getBlockByName(name: String): BlockEntity?

    @Transaction
    suspend fun insertBlockWithDaysWithSessionsWithExercisesWithMovementAndSets(
        block: BlockEntity,
        days: List<DayEntity>,
        sessions: List<SessionEntity>,
        exercises: List<ExerciseEntity>,
        movements: List<MovementEntity>,
        sets: List<SetEntity>,
        dayDao: DayDao,
        sessionDao: SessionDao,
        exerciseDao: ExerciseDao,
        movementDao: MovementDao,
        setDao: SetDao
    ): Long {
        val blockId = insertBlock(block)
        days.forEach { dayDao.insertDay(it) }
        sessions.forEach { sessionDao.insertSession(it) }
        movements.forEach {
            movementDao.insertMovement(it, onConflict = OnConflictStrategy.IGNORE)
        }
        exercises.forEach { exerciseDao.insertExercise(it) }
        sets.forEach { setDao.insertSet(it) }
        return blockId
    }
}
