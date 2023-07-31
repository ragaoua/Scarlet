package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.presentation.session.util.SetFieldType

class CopyPreviousSetFieldUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(
        set: Set,
        sessionExercises: List<ExerciseWithMovementAndSets>,
        fieldToCopy: SetFieldType
    ): SimpleResource {

        val previousSet =
            sessionExercises
                .find { it.exercise.id == set.exerciseId }
                ?.sets
                ?.find { it.order == set.order - 1 }
                ?: return Resource.Error(Errors.NoPreviousSetFound)

        repository.updateSet(
            when (fieldToCopy) {
                SetFieldType.REPS -> set.copy(reps = previousSet.reps)
                SetFieldType.WEIGHT -> set.copy(weight = previousSet.weight)
                SetFieldType.RPE -> set.copy(rpe = previousSet.rpe)
            }
        )

        return Resource.Success()
    }

    sealed interface Errors {
        object NoPreviousSetFound: Errors
    }
}