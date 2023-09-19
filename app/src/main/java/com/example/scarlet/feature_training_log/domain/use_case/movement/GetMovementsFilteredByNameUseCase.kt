package com.example.scarlet.feature_training_log.domain.use_case.movement

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMovementsFilteredByNameUseCase(
    val repository: ScarletRepository
) {

    private var movements: Flow<List<Movement>>? = null

    /**
     * Retrieve a list of movements filtered by name (the movements' name has to contain the filter)
     * The filter is case insensitive.
     *
     * @param nameFilter movement name filter
     *
     * @return a flow of resources with data (the list of movements)
     */
    operator fun invoke(nameFilter: String): Flow<Resource<List<Movement>>> {
        return (movements ?: run { repository.getAllMovements() })
            .also{ movements = it }
            .map { movementList ->
                movementList
                    .filter { it.name.contains(nameFilter, ignoreCase = true) }
                    .sortedBy { it.name }
                    .let { Resource.Success(it) }
            }
    }
}