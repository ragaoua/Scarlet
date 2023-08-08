package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class InsertSetUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(
        exerciseId: Int,
        exerciseSets: List<Set>
    ): Resource<Long> {
        val set = Set(
            exerciseId = exerciseId,
            order = exerciseSets.count() + 1
        )

        return repository.insertSet(set)
            .let { Resource.Success(it) }
    }
}