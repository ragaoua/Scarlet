package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.BlockWithDays
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllBlocksUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Retrieve a list of all blocks with all days and sessions.
     * The block are sorted by latest session's date descending then by id descending.
     * The sessions are sorted by date.
     *
     * @return a flow of resources data (list of blocks)
     */
    operator fun invoke(): Flow<Resource<List<BlockWithDays<DayWithSessions<Session>>>>> {
        return repository.getAllBlocks()
            .map { blocks ->
                blocks.map { block -> block.copy(
                    days = block.days.map { day -> day.copy(
                        sessions = day.sessions.sortedBy { it.date }
                    )}
                )}.sortedWith(
                    compareByDescending<BlockWithDays<DayWithSessions<Session>>> { block ->
                        block.days.flatMap { it.sessions }.maxOfOrNull { it.date }
                    }.thenByDescending { block -> block.id }
                ).let { Resource.Success(it) }
            }
    }
}