package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class DeleteExerciseUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(
        exercise: Exercise,
        sessionExercises: List<Exercise>
    ): SimpleResource {

        repository.deleteExercise(exercise)

        // Update the order of the other exercises if necessary
        sessionExercises
            .filter { it.order > exercise.order }
            .forEach { repository.updateExercise(it.copy(order = it.order - 1)) }

        return Resource.Success()
    }
}