package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllBlocksUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Retrieve a list of all blocks with all sessions.
     * The block are sorted by latest session's date descending then by id descending.
     * The sessions of each block are sorted by date.
     *
     * @return a flow of resources data (list of blocks)
     */
    operator fun invoke(): Flow<Resource<List<BlockWithSessions<Session>>>> {
        return repository.getAllBlocksWithSessions()
            .map { blocks ->
                blocks.map { block -> block.copy(
                    sessions = block.sessions.sortedBy { it.date }
                )}.sortedWith(
                    compareByDescending<BlockWithSessions<Session>> { block ->
                        block.sessions.maxOfOrNull { it.date }
                    }.thenByDescending { block -> block.id }
                ).let { Resource.Success(it) }
            }
    }
}