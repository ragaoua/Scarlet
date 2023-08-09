package com.example.scarlet.feature_training_log.domain.use_case.training_log

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetActiveBlockUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Retrieve the active block (completed is false)
     *
     * @return a flow of resources with an error or data (the active block)
     */
    operator fun invoke(): Flow<Resource<BlockWithSessions?>> {
        return repository.getBlocksWithSessionsByCompleted(false)
            .map { activeBlocks ->
                if (activeBlocks.size > 1) {
                    Resource.Error(StringResource(R.string.error_too_many_active_blocks))
                } else {
                    val activeBlock = activeBlocks.firstOrNull()
                    activeBlock?.copy(
                        sessions = activeBlock.sessions.sortedBy { it.date }
                    ).let { Resource.Success(it) }
                }
            }
    }
}