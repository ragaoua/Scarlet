package com.example.scarlet.feature_training_log.domain.use_case.training_log

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.BlockWithList
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
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
    operator fun invoke(): Flow<Resource<List<BlockWithList<DayWithSessions>>>> {
        return repository.getAllBlocksWithSessions()
            .map { blocks ->
                blocks.map { block ->
                    block.copy(
                        days = block.days.map {day ->
                            day.copy(
                                sessions = day.sessions.sortedBy { it.date }
                            )
                        }
                    )
                }
                .sortedWith(compareByDescending<BlockWithList<DayWithSessions>> { block ->
                    block.days.flatMap { it.sessions }.lastOrNull()?.date
                }
                    .thenByDescending { block -> block.id }
                ).let { Resource.Success(it) }
            }
    }
}