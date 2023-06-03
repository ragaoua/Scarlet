package com.example.scarlet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.scarlet.db.ScarletRepository

class TrainingLogViewModelFactory(
    private val repository: ScarletRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TrainingLogViewModel(repository) as T
}