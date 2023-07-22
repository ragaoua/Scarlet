package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMovementsFilteredByNameUseCase(
    val repository: ScarletRepository
) {

    private var movements: Flow<List<Movement>>? = null

    operator fun invoke(nameFilter: String): Flow<Resource<List<Movement>>> {
        return (movements ?: run { repository.getAllMovements() })
            .also{ movements = it }
            .map { movementList ->
                Resource.Success(
                    movementList
                        .filter { it.name.contains(nameFilter, ignoreCase = true) }
                        .sortedBy { it.name }
                )
            }
    }
}