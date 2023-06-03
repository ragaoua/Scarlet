package com.example.scarlet.db

import com.example.scarlet.dao.BlockDao
import com.example.scarlet.model.Block
import kotlinx.coroutines.flow.Flow

class ScarletRepositoryImpl(
    private val blockDao: BlockDao
) : ScarletRepository {

    override fun getBlocksByCompleted(completed: Boolean): Flow<List<Block>> {
        return blockDao.getBlocksByCompleted(completed)
    }
}