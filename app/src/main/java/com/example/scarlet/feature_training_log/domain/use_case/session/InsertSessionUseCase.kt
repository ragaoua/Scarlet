package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.feature_training_log.domain.model.ISession
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class InsertSessionUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Insert a session.
     * If sessions already exist for the given day, the new session will copy
     * the exercises from the latest one (only the exercises, not the sets).
     *
     * @param dayId day id for the session to be inserted
     *
     * @return the id of the inserted session
     */
    suspend operator fun invoke(dayId: Long): Long {
        val latestSessionForThatDay = repository.getSessionsWithExercisesByDayId(dayId)
            .sortedWith(compareByDescending<ISession> { it.date }.thenByDescending { it.id })
            .firstOrNull()

        val insertedSession = Session(dayId = dayId)
        val exercises = latestSessionForThatDay?.exercises?.map {
            // Setting the id to 0 shouldn't be necessary, but it's here just in case
            it.copy(
                id = 0,
                sessionId = 0
            )
        } ?: emptyList()

        return repository.insertSessionWithExercises(insertedSession, exercises)
    }
}