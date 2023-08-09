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
     * @param session session to be inserted
     *
     * @return resource with data (the inserted session's id)
     */
    suspend operator fun invoke(session: Session): Resource<Long> {
        return repository.insertSession(session)
            .let { Resource.Success(it) }
    }
}