package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class InsertExerciseUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Insert an exercise
     *
     * @param exercise exercise to be inserted
     *
     * @return a resource with data (id of the inserted exercise)
     */
    suspend operator fun invoke(exercise: Exercise): Resource<Long> {
        return repository.insertExercise(exercise)
            .let { Resource.Success(it) }
    }
}