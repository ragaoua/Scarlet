package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class UpdateSessionUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(session: Session) {
        repository.updateSession(session)
    }
}