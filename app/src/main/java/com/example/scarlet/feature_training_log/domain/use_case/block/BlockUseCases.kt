package com.example.scarlet.feature_training_log.domain.use_case.block

data class BlockUseCases(
    val getDaysWithSessionsWithMovementAndSetsByBlockId: GetDaysWithSessionsWithMovementAndSetsByBlockIdUseCase,
    val insertSession: InsertSessionUseCase,
    val updateBlock: UpdateBlockUseCase,
    val deleteSession: DeleteSessionUseCase
)