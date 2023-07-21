package com.example.scarlet.feature_training_log.domain.use_case.training_log

data class TrainingLogUseCases(
    val getActiveBlock: GetActiveBlockUseCase,
    val getCompletedBlocks: GetCompletedBlocksUseCase
)