package com.example.scarlet.feature_training_log.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.SessionWithMovements
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM session")
    fun getAllSessions(): List<Session>

    @Query("SELECT * FROM session WHERE id = :sessionId")
    fun getSessionById(sessionId: Int): Flow<Session?>

    @Transaction
    @Query("""
        SELECT session.*
        FROM session
        WHERE blockId = :blockId
    """)
    fun getSessionsWithMovementsByBlockId(blockId: Int): Flow<List<SessionWithMovements>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSession(session: Session): Long

    @Update
    suspend fun updateSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)
}
