package com.example.scarlet.feature_training_log.domain.use_case.block

data class BlockUseCases(
    val getDaysWithSessionsWithMovementAndSetsByBlockId: GetDaysWithSessionsWithMovementAndSetsByBlockIdUseCase,
    val updateBlock: UpdateBlockUseCase,
    val insertSession: InsertSessionUseCase,
    val updateSession: UpdateSessionUseCase,
    val deleteSession: DeleteSessionUseCase,
    val getMovementsFilteredByName: GetMovementsFilteredByNameUseCase,
    val insertExercise: InsertExerciseUseCase,
    val updateExercise: UpdateExerciseUseCase,
    val insertMovement: InsertMovementUseCase,
    val deleteExercise: DeleteExerciseUseCase,
    val insertSet: InsertSetUseCase,
    val updateSet: UpdateSetUseCase,
    val deleteSet: DeleteSetUseCase,
    val copyPrecedingSetField: CopyPrecedingSetFieldUseCase,
    val updateLoadBasedOnPreviousSet: UpdateLoadBasedOnPreviousSetUseCase
)