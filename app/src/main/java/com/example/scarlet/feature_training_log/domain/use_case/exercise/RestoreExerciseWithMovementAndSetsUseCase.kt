package com.example.scarlet.feature_training_log.domain.use_case.exercise

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class RestoreExerciseWithMovementAndSetsUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Insert an exercise along with its movement and sets.
     * This is meant to be used when restoring data (on an "undo delete")
     * so it does not validate anything regarding the inserted data.
     *
     * @param exercise the exercise to insert
     *
     * @return a resource with an error if found, or a simple resource with no data
     */
    suspend operator fun invoke(exercise: ExerciseWithMovementAndSets): SimpleResource {
        repository.insertExerciseWithMovementAndSets(
            exercises = exercise.toExercise(),
            movements = exercise.movement,
            sets = exercise.sets
        )

        return Resource.Success()
    }

}