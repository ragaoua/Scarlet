package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.SessionWithMovements
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSessionsWithMovementsByBlockIdUseCase(
    private val repository: ScarletRepository
) {
    operator fun invoke(blockId: Int): Flow<Resource<List<SessionWithMovements>>> {
        return repository.getSessionsWithMovementsByBlockId(blockId)
            .map { Resource.Success(it) } // TODO : sort by exercise order
    }
}