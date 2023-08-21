package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class UpdateSessionUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Update a session
     *
     * @param session session to be updated
     */
    suspend operator fun invoke(session: Session) {
        println("UpdateSessionUseCase: updating session $session")
        repository.updateSession(session)
    }
}