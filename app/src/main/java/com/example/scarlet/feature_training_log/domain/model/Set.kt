package com.example.scarlet.feature_training_log.domain.model

import java.io.Serializable

data class Set(
    val id: Long = 0,
    val exerciseId: Long = 0,
    val order: Int = 0,
    val reps: Int? = null,
    val load: Float? = null,
    val rating: Float? = null
) : Serializable