package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllMovementsUseCase(
    val repository: ScarletRepository
) {

    operator fun invoke(): Flow<Resource<List<Movement>>> {
        return repository.getAllMovements()
            .map { Resource.Success(it) }
    }
}