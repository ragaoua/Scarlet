package com.example.scarlet.feature_training_log.domain.use_case.training_log

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllBlocksUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Retrieve a list of all blocks
     *
     * @return a flow of resources with an error or data (list of blocks)
     */
    operator fun invoke(): Flow<Resource<List<BlockWithSessions>>> {
        return repository.getAllBlocksWithSessions()
            .map { blocksWithSession ->
                blocksWithSession
                    .map { blockWithSessions ->
                        blockWithSessions.copy(
                            sessions = blockWithSessions.sessions
                                .sortedBy { it.date }
                        )
                    }
                    .sortedWith(compareByDescending<BlockWithSessions>
                        { it.sessions.lastOrNull()?.date }.thenByDescending { it.block.id }
                    ).let { Resource.Success(it) }
            }
    }
}