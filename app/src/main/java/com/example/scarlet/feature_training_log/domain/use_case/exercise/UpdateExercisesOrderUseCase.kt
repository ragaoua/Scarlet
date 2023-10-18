package com.example.scarlet.feature_training_log.domain.use_case.exercise

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class UpdateExercisesOrderUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Update the order of a list of exercises and update
     * the order of the other exercises if necessary
     *
     * Check that the exercises share the same session id
     * and order. If not, returns an error
     *
     * @param exercises list of exercises to update
     * @param newOrder new order of the exercises
     */
    suspend operator fun invoke(exercises: List<Exercise>, newOrder: Int): SimpleResource {
        if (exercises.groupBy { it.sessionId }.size > 1) {
            return Resource.Error(
                StringResource(R.string.error_exercises_must_share_the_same_session_id)
            )
        }
        if (exercises.groupBy { it.order }.size > 1) {
            return Resource.Error(
                StringResource(R.string.error_exercises_must_share_the_same_order)
            )
        }

        repository.updateExerciseOrder(exercises, newOrder)
        return Resource.Success()
    }
}