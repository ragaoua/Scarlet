package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithDays
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class RestoreBlockWithDaysWithSessionsWithExercisesWithSetsUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Insert a block along with all its cascading data.
     * This is meant to be used when restoring data (on an "undo")
     * so it does not validate the block name or anything else
     * regarding the inserted data
     *
     *
     * @param block the block to insert
     *
     * @return a resource with an error if found, or a simple resource with no data
     */
    suspend operator fun invoke(
        block: BlockWithDays<DayWithSessions<SessionWithExercises<ExerciseWithMovementAndSets>>>
    ): Resource<Block> {
        val days = block.days
        val sessions = days.flatMap { it.sessions }
        val exercises = sessions.flatMap { it.exercises }
        val sets = exercises.flatMap { it.sets }

        repository.insertBlockWithDaysWithSessionsWithExercisesWithSets(
            block = block.toBlock(),
            days = days.map { it.toDay() },
            sessions = sessions.map { it.toSession() },
            exercises = exercises.map { it.toExercise() },
            sets = sets
        )

        return Resource.Success()
    }

}