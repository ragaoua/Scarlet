package com.example.scarlet.feature_training_log.domain.use_case.exercise

import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class UpdateExerciseUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Update an exercise
     *
     * @param exercise exercise to be updated
     */
    suspend operator fun invoke(exercise: Exercise) {
        repository.updateExercise(exercise)
    }
}