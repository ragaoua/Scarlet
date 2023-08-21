package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class InsertSessionUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Insert a session
     *
     * @param dayId day id for the session to be inserted
     *
     * @return resource with data (the inserted session)
     */
    suspend operator fun invoke(dayId: Long): Resource<Session> {
        val session = Session(dayId = dayId)

        repository.insertSession(session)
            .also {
                return Resource.Success(
                    session.copy(id = it)
                )
            }
    }
}