package com.example.scarlet.feature_training_log.data.data_source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.scarlet.feature_training_log.data.data_source.entity.SetEntity

@Dao
interface SetDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSet(set: SetEntity): Long

    @Update
    suspend fun updateSet(set: SetEntity)

    @Delete
    suspend fun deleteSet(set: SetEntity)

    @Query("SELECT * FROM `set` WHERE exerciseId = :exerciseId")
    suspend fun getSetsByExerciseId(exerciseId: Long): List<SetEntity>

    @Transaction
    suspend fun deleteSetAndUpdateSubsequentSetsOrder(set: SetEntity) {
        deleteSet(set)

        getSetsByExerciseIdWhereOrderIsGreaterThan(
            exerciseId = set.exerciseId,
            setOrder = set.order
        ).forEach {
            updateSet(it.copy(order = it.order - 1))
        }
    }

    @Transaction
    suspend fun insertSetWhileSettingOrder(set: SetEntity): Long {
        val order = getSetsByExerciseId(set.exerciseId).size + 1

        return insertSet(set.copy(order = order))
    }

    @Query("""
        SELECT *
        FROM `set`
        WHERE exerciseId = :exerciseId
        AND `order` > :setOrder
    """)
    suspend fun getSetsByExerciseIdWhereOrderIsGreaterThan(exerciseId: Long, setOrder: Int): List<SetEntity>

    @Transaction
    suspend fun restoreSet(set: SetEntity) {
        getSetsByExerciseIdWhereOrderIsGreaterThan(
            exerciseId = set.exerciseId,
            setOrder = set.order - 1
        ).sortedByDescending { it.order } /* Sorting is necessary to satisfy the constraint
                                             that (exerciseId,order) must be unique */
            .forEach {
                updateSet(it.copy(order = it.order + 1))
            }
        insertSet(set)
    }

}
