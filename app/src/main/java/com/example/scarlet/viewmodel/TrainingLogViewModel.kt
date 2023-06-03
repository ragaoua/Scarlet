package com.example.scarlet.viewmodel

import androidx.lifecycle.ViewModel
import com.example.scarlet.db.ScarletRepository
import com.example.scarlet.model.Block

class TrainingLogViewModel(
    private val repository: ScarletRepository
) : ViewModel() {

    val completedBlocks = repository.getBlocksByCompleted(true)

    fun getActiveBlock(): Block? {
        /* TODO */
        return Block(1, "lorem", false)
    }
}