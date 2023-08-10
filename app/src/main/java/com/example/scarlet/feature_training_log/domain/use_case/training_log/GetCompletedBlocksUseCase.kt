package com.example.scarlet.feature_training_log.domain.use_case.training_log

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCompletedBlocksUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Retrieve a list of completed block (completed is true)
     *
     * @return a flow of resources with an error or data (list of completed blocks)
     */
    operator fun invoke(): Flow<Resource<List<BlockWithSessions>>> {
        return repository.getBlocksWithSessionsByCompleted(true)
            .map { blocksWithSession ->
                blocksWithSession
                    .map { blockWithSessions ->
                        blockWithSessions.copy(
                            sessions = blockWithSessions.sessions
                                .sortedBy { it.date }
                        )
                    }
                    .sortedByDescending { it.sessions.lastOrNull()?.date }
                    .let { Resource.Success(it) }
            }
    }
}