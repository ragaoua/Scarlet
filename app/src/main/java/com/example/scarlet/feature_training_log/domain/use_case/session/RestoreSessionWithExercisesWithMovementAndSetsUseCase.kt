package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class RestoreSessionWithExercisesWithMovementAndSetsUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Insert a session along with all its cascading data.
     * This is meant to be used when restoring data (on an "undo delete")
     * so it does not validate anything regarding the inserted data.
     *
     * @param session the session to insert
     *
     * @return a resource with an error if found, or a simple resource with no data
     *
     * Note: this won't be unit tested because it's a simple call to the repository
     */
    suspend operator fun invoke(
        session: SessionWithExercises<ExerciseWithMovementAndSets>
    ): Resource<Block> {
        val exercises = session.exercises
        val movements = exercises.map { it.movement }
        val sets = exercises.flatMap { it.sets }

        repository.insertSessionWithExercisesWithMovementAndSets(
            session = session.toSession(),
            exercises = exercises.map { it.toExercise() },
            movements = movements,
            sets = sets
        )

        return Resource.Success()
    }

}