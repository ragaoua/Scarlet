package com.example.scarlet.feature_training_log.domain.use_case.exercise

import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class UpdateExerciseSupersetOrderUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Update the supersetOrder of an exercise and update
     * the supersetOrder of the other exercises if necessary
     *
     * @param exercise exercise to update
     * @param newSupersetOrder new supersetOrder of the exercise
     */
    suspend operator fun invoke(exercise: Exercise, newSupersetOrder: Int) {
        repository.updateExerciseSupersetOrder(exercise, newSupersetOrder)
    }
}