package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class InsertSetUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Insert a set.
     *
     * @param exerciseId id of the set's exercise
     * @param exerciseSets list of that exercise's sets (to determine the set order)
     *
     * @return a resource with data (id of the inserted set)
     */
    suspend operator fun invoke(
        exerciseId: Long,
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