package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class InsertSessionUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Insert a session
     *
     * @param blockId block id for the session to be inserted
     *
     * @return resource with data (the inserted session)
     */
    suspend operator fun invoke(blockId: Long): Resource<Session> {
        // In the future, the day id will/should be provided as an argument to the use case
        // For now, we assume that a block only has one day associated, to to simplify
        // things by getting the first day out of all days of the block...
        val session = Session(dayId = repository.getDaysByBlockId(blockId).first().id)

        repository.insertSession(session)
            .also {
                return Resource.Success(
                    session.copy(id = it)
                )
            }
    }
}