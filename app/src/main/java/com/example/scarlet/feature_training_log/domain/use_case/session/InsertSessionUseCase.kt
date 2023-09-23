package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
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
     * @return resource with data (the inserted session)
     */
    suspend operator fun invoke(dayId: Long): SimpleResource {
        val daySessions = repository.getSessionsWithExercisesByDayId(dayId)

        val session = Session(dayId = dayId)
        val exercises = daySessions.lastOrNull()?.exercises?.map {
            // Sets the ids to 0 shouldn't be necessary, but it's here just in case
            it.copy(
                id = 0,
                sessionId = 0
            )
        } ?: emptyList()
        repository.insertSessionWithExercises(session, exercises)

        return Resource.Success()
    }
}