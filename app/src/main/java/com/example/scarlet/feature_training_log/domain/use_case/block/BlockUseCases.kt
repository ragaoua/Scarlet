package com.example.scarlet.feature_training_log.domain.use_case.block

data class BlockUseCases(
    val getSessionsWithMovementsByBlockId: GetSessionsWithMovementsByBlockIdUseCase,
    val insertSession: InsertSessionUseCase,
    val updateBlock: UpdateBlockUseCase,
    val deleteSession: DeleteSessionUseCase
)