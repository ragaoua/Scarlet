package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class DeleteBlockUseCase(
    private val repository: ScarletRepository
) {

    /**
     * Delete a block
     *
     * @param block block to be deleted
     *
     * Note: this won't be unit tested because it simply calls a repository method
     */
    suspend operator fun invoke(block: Block) {
        repository.deleteBlock(block)
    }

}