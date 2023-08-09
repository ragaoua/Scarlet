package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class DeleteSetUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Delete a set, then update the order of the subsequent sets for the same exercise
     *
     * @param set set to be deleted
     * @param exerciseSets list of that exercise's sets
     */
    suspend operator fun invoke(
        set: Set,
        exerciseSets: List<Set>
    ) {
        repository.deleteSet(set)

        // Update the order of the other sets if necessary
        exerciseSets
            .filter { it.order > set.order }
            .forEach { repository.updateSet(it.copy(order = it.order - 1)) }
    }
}