package com.example.scarlet.feature_training_log.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.scarlet.feature_training_log.domain.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM session")
    fun getAllSessions(): List<Session>

    @Query("SELECT * FROM session WHERE id = :sessionId")
    fun getSessionById(sessionId: Int): Flow<Session?>

    @Query("SELECT * FROM session WHERE blockId = :blockId")
    fun getSessionsByBlockId(blockId: Int): Flow<List<Session>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSession(session: Session)

    @Update
    suspend fun updateSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)
}
