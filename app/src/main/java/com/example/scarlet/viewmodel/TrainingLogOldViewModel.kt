package com.example.scarlet.viewmodel

import androidx.lifecycle.ViewModel
import com.example.scarlet.db.ScarletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrainingLogOldViewModel @Inject constructor(
    private val repository: ScarletRepository
) : ViewModel() {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////// SET /////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    fun getExerciseSetsById(exerciseId: Int) = repository.getExerciseSetsById(exerciseId)

}