package com.example.scarlet.db

import com.example.scarlet.model.Block
import com.example.scarlet.model.Session
import kotlinx.coroutines.flow.Flow

class ScarletRepository(
    private val dbInstance: ScarletDatabase
) {

    fun getBlocksByCompleted(completed: Boolean): Flow<List<Block>> {
        return dbInstance.blockDao.getBlocksByCompleted(completed)
    }

    fun getSessionsByBlockId(blockId: Int): Flow<List<Session>> {
        return dbInstance.sessionDao.getSessionsByBlockId(blockId)
    }
}