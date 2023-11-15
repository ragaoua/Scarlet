package com.example.scarlet.feature_training_log.domain.use_case.set

import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class InsertEmptySetWhileSettingsOrderUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Insert an empty set. The order is defined by the number of sets
     * for the exercise.
     *
     * @param exerciseId id of the set's exercise
     *
     * @return a simple resource with no data
     *
     * Note: this won't be unit tested because it's a simple call to the repository
     */
    suspend operator fun invoke(exerciseId: Long) {
        repository.insertSetWhileSettingOrder(
            Set(exerciseId = exerciseId)
        )
    }
}