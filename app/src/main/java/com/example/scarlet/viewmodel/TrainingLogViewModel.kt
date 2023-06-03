package com.example.scarlet.viewmodel

import androidx.lifecycle.ViewModel
import com.example.scarlet.db.ScarletRepository

class TrainingLogViewModel(
    repository: ScarletRepository
) : ViewModel() {

    val completedBlocks = repository.getBlocksByCompleted(true)
    val activeBlocks = repository.getBlocksByCompleted(false)

}