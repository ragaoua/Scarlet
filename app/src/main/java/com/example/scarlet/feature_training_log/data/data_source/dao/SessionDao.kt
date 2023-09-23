package com.example.scarlet.feature_training_log.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.scarlet.feature_training_log.data.data_source.entity.ExerciseEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.SessionEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.SessionWithExercisesEntity

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSession(session: SessionEntity): Long

    @Transaction
    suspend fun insertSessionWithExercises(
        session: SessionEntity,
        exercises: List<ExerciseEntity>,
        exerciseDao: ExerciseDao
    ): Long {
        val sessionId = insertSession(session)

        exercises.forEach { exercise ->
            exerciseDao.insertExercise(exercise.copy(
                sessionId = sessionId
            ))
        }

        return sessionId
    }

    @Update
    suspend fun updateSession(session: SessionEntity)

    @Delete
    suspend fun deleteSession(session: SessionEntity)

    @Query("SELECT * FROM session WHERE dayId = :dayId")
    suspend fun getSessionsWithExercisesByDayId(dayId: Long): List<SessionWithExercisesEntity>
}
