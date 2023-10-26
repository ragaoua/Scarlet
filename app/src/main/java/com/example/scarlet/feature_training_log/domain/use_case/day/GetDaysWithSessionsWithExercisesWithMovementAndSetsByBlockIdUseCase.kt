package com.example.scarlet.feature_training_log.domain.use_case.day

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.IExercise
import com.example.scarlet.feature_training_log.domain.model.ISession
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDaysWithSessionsWithExercisesWithMovementAndSetsByBlockIdUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Retrieve a list of days for a given block
     * Each day's sessions are sorted by date
     * Each session's exercises are sorted by order then by supersetOrder
     * Each exercise's sets are sorted by order
     *
     * @param blockId id of the days' block
     *
     * @return a flow of resources with data (the list of days)
     */
    operator fun invoke(blockId: Long):
            Flow<Resource<List<DayWithSessions<SessionWithExercises<ExerciseWithMovementAndSets>>>>> {
        return repository.getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockId(blockId)
            .map { days ->
                days.sortedBy { it.order }
                    .map { day -> day.copy(
                        sessions = day.sessions
                            .sortedWith(
                                compareBy<ISession> {
                                    it.date
                                }.thenBy { it.id }
                            ).map { session -> session.copy(
                                exercises = session.exercises
                                    .sortedWith(
                                        compareBy<IExercise> {
                                            it.order
                                        }.thenBy { it.supersetOrder }
                                    ).map { exercise -> exercise.copy(
                                        movement = exercise.movement,
                                        sets = exercise.sets.sortedBy { it.order }
                                    )}
                            )}
                    )}.let { Resource.Success(it) }
            }
    }
}