package com.example.scarlet.db

import com.example.scarlet.model.Block
import kotlinx.coroutines.flow.Flow

interface ScarletRepository {

    fun getBlocksByCompleted(completed: Boolean): Flow<List<Block>>
}