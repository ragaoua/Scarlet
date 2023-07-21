package com.example.scarlet.feature_training_log.domain.use_case.training_log

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetActiveBlockUseCase(
    private val repository: ScarletRepository
) {

    operator fun invoke(): Flow<Resource<BlockWithSessions?>> {
        return repository.getBlocksWithSessionsByCompleted(false)
            .map { activeBlocks ->
                if (activeBlocks.size > 1) {
                    Resource.Error(ActiveBlockErrors.TooManyActiveBlocks)
                } else {
                    Resource.Success(activeBlocks.firstOrNull()?.copy(
                        sessions = activeBlocks.first().sessions
                            .sortedBy {
                                it.date
                            }
                    ))
                }
            }
    }

    sealed interface ActiveBlockErrors {
        object TooManyActiveBlocks : ActiveBlockErrors
    }

}