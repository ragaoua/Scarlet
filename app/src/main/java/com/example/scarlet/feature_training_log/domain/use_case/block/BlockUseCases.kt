package com.example.scarlet.feature_training_log.domain.use_case.block

data class BlockUseCases(
    val getDaysWithSessionsWithMovementsByBlockId: GetDaysWithSessionsWithMovementsByBlockIdUseCase,
    val insertSession: InsertSessionUseCase,
    val updateBlock: UpdateBlockUseCase,
    val deleteSession: DeleteSessionUseCase
)