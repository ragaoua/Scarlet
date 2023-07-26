package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class UpdateExerciseUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(exercise: Exercise): SimpleResource {
        repository.updateExercise(exercise)
        return Resource.Success()
    }
}