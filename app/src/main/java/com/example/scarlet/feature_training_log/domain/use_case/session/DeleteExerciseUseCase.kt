package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class DeleteExerciseUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Delete an exercise, then update the order of the subsequent exercises for the same session
     *
     * @param exercise exercise to be deleted
     * @param sessionExercises list of that session's exercises
     */
    suspend operator fun invoke(
        exercise: Exercise,
        sessionExercises: List<Exercise>
    ) {
        repository.deleteExercise(exercise)

        sessionExercises
            .filter { it.order > exercise.order }
            .forEach { repository.updateExercise(it.copy(order = it.order - 1)) }
    }
}