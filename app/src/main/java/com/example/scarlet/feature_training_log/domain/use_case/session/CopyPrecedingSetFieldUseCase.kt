package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.presentation.block.util.SetFieldType

class CopyPrecedingSetFieldUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Update a set field with data from the previous set,
     * after checking that the previous set exists
     *
     * @param set the set to be updated
     * @param sessionExercises list of that exercise's sets
     * @param fieldToCopy field to be copied
     *
     * @return a resource with an error if the set isn't preceded by another one,
     * or a simple resource with no data
     */
    suspend operator fun invoke(
        set: Set,
        sessionExercises: List<ExerciseWithMovementAndSets>,
        fieldToCopy: SetFieldType
    ): SimpleResource {

        val precedingSet = sessionExercises
            .find { it.id == set.exerciseId }
            ?.sets
            ?.find { it.order == set.order - 1 }
            ?: return Resource.Error(StringResource(R.string.error_no_previous_set))

        repository.updateSet(
            when (fieldToCopy) {
                SetFieldType.REPS -> set.copy(reps = precedingSet.reps)
                SetFieldType.WEIGHT -> set.copy(weight = precedingSet.weight)
                SetFieldType.RPE -> set.copy(rpe = precedingSet.rpe)
            }
        )

        return Resource.Success()
    }
}