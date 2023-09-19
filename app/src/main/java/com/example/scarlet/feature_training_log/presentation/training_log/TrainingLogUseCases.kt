package com.example.scarlet.feature_training_log.presentation.training_log

import com.example.scarlet.feature_training_log.domain.use_case.block.DeleteBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.GetAllBlocksUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.InsertBlockUseCase

data class TrainingLogUseCases(
    val getAllBlocks: GetAllBlocksUseCase,
    val deleteBlock: DeleteBlockUseCase,
    val insertBlock: InsertBlockUseCase
)