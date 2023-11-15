package com.example.scarlet.feature_training_log.domain.use_case.set

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class RestoreSetUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Insert a set.
     * This is meant to be used when restoring data (on an "undo delete")
     * so it does not validate anything regarding the inserted data.
     *
     * @param set the set to insert
     *
     * @return a resource with an error if found, or a simple resource with no data
     *
     * Note: this won't be unit tested because it's a simple call to the repository
     */
    suspend operator fun invoke(set: Set): SimpleResource {
        repository.restoreSet(set)

        return Resource.Success()
    }

}