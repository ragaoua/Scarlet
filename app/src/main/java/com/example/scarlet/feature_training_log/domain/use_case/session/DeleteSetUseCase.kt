package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class DeleteSetUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(
        set: Set,
        exerciseSets: List<Set>
    ): SimpleResource {

        repository.deleteSet(set)

        // Update the order of the other sets if necessary
        exerciseSets
            .filter { it.order > set.order }
            .forEach { repository.updateSet(it.copy(order = it.order - 1)) }

        return Resource.Success()
    }
}