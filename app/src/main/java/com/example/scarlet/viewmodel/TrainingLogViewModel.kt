package com.example.scarlet.viewmodel

import androidx.lifecycle.ViewModel
import com.example.scarlet.model.Block

class TrainingLogViewModel : ViewModel() {

    fun getActiveBlock(): Block? {
        return Block("lorem", false)
    }

}