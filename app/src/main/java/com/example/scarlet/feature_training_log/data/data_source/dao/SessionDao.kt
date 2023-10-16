package com.example.scarlet.feature_training_log.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.scarlet.feature_training_log.data.data_source.entity.ExerciseEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.MovementEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.SessionEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.SessionWithExercisesEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.SetEntity

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

    @Transaction
    @Query("SELECT * FROM session WHERE dayId = :dayId")
    suspend fun getSessionsWithExercisesByDayId(dayId: Long): List<SessionWithExercisesEntity>

    @Transaction
    suspend fun insertSessionWithExercisesWithMovementAndSets(
        session: SessionEntity,
        exercises: List<ExerciseEntity>,
        movements: List<MovementEntity>,
        sets: List<SetEntity>,
        exerciseDao: ExerciseDao,
        movementDao: MovementDao,
        setDao: SetDao
    ): Long {
        val sessionId = insertSession(session)
        movements.forEach {
            movementDao.insertMovement(it, onConflict = OnConflictStrategy.IGNORE)
        }
        exercises.forEach { exerciseDao.insertExercise(it) }
        sets.forEach { setDao.insertSet(it) }

        return sessionId
    }
}
