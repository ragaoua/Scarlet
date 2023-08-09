package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class DeleteSessionUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Delete a session
     *
     * @param session session to be deleted
     */
    suspend operator fun invoke(session: Session) {
        repository.deleteSession(session)
    }
}