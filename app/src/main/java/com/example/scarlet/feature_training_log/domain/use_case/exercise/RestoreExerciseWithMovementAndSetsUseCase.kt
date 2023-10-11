package com.example.scarlet.feature_training_log.domain.use_case.exercise

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
     * @param isExerciseInASuperset whether the exercise is in a superset or not
     */
    suspend operator fun invoke(
        exercise: ExerciseWithMovementAndSets,
        isExerciseInASuperset: Boolean
    ) {
        if (isExerciseInASuperset) {
            repository.insertSupersetExerciseWithMovementAndSets(
                exercises = exercise.toExercise(),
                movements = exercise.movement,
                sets = exercise.sets
            )
        } else {
            repository.insertExerciseWithMovementAndSets(
                exercises = exercise.toExercise(),
                movements = exercise.movement,
                sets = exercise.sets
            )
        }
    }

}