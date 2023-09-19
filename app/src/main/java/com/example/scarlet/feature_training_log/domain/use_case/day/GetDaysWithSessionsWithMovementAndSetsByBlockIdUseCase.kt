package com.example.scarlet.feature_training_log.domain.use_case.day

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDaysWithSessionsWithMovementAndSetsByBlockIdUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Retrieve a list of days for a given block
     * Each day's sessions are sorted by date
     * Each session's exercises are sorted by order
     * Each exercise's sets are sorted by order
     *
     * @param blockId id of the days' block
     *
     * @return a flow of resources with data (the list of days)
     */
    operator fun invoke(blockId: Long):
            Flow<Resource<List<DayWithSessions<SessionWithExercises<ExerciseWithMovementAndSets>>>>> {
        return repository.getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockId(blockId)
            .map { list ->
                list.map { day ->
                    day.copy(
                        sessions = day.sessions
                            .sortedWith(
                                compareBy<SessionWithExercises<ExerciseWithMovementAndSets>> {
                                    it.date
                                }.thenBy { it.id }
                            ).map { session ->
                                session.copy(
                                    exercises = session.exercises
                                        .sortedBy { it.order }
                                        .map { exercise ->
                                            exercise.copy(
                                                movement = exercise.movement,
                                                sets = exercise.sets
                                                    .sortedBy { exercise.order }
                                            )
                                        }
                                )
                            }
                    )
                }.let { Resource.Success(it) }
            }
    }
}