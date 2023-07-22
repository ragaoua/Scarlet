package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GetMovementsFilteredByNameUseCase(
    val repository: ScarletRepository
) {

    operator fun invoke(nameFilterState: MutableStateFlow<String>): Flow<Resource<List<Movement>>> {
        return combine(
            repository.getAllMovements(),
            nameFilterState
        ) { movements, nameFilter ->
            movements.filter { it.name.contains(nameFilter, ignoreCase = true) }
        }.map { Resource.Success(it) }
    }
}