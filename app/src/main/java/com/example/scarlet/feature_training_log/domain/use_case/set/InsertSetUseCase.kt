package com.example.scarlet.feature_training_log.domain.use_case.set

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class InsertSetUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Insert a set. The order is defined by the number of sets
     * for the exercise.
     *
     * @param exerciseId id of the set's exercise
     *
     * @return a simple resource with no data
     */
    suspend operator fun invoke(exerciseId: Long): SimpleResource {
        repository.insertSetWhileSettingOrder(
            Set(exerciseId = exerciseId)
        )
        
        return Resource.Success()
    }
}