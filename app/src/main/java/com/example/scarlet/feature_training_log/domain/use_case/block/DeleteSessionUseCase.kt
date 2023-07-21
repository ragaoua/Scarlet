package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class DeleteSessionUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(session: Session): SimpleResource {
        repository.deleteSession(session)
        return Resource.Success()
    }
}