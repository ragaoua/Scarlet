package com.example.scarlet.feature_training_log.domain.use_case.training_log

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCompletedBlocksUseCase(
    private val repository: ScarletRepository
) {
    operator fun invoke(): Flow<Resource<List<BlockWithSessions>>> {
        return repository.getBlocksWithSessionsByCompleted(true)
            .map { blocksWithSession ->
                blocksWithSession
                    .sortedByDescending { it.sessions.lastOrNull()?.date }
                    .map { blockWithSessions ->
                        blockWithSessions.copy(
                            sessions = blockWithSessions.sessions
                                .sortedBy { it.date }
                        )
                    }
                    .let { Resource.Success(it) }
            }
    }
}