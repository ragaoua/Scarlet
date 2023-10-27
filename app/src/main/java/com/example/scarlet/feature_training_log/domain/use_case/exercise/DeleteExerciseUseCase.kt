package com.example.scarlet.feature_training_log.domain.use_case.exercise

import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class DeleteExerciseUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Delete an exercise and update the order of the subsequent exercises for the same session
     *
     * @param exercise exercise to be deleted
     *
     * Note: this won't be unit tested because it's a simple call to the repository
     */
    suspend operator fun invoke(exercise: Exercise) {
        repository.deleteExerciseAndUpdateSubsequenceExercisesOrder(exercise)
    }
}