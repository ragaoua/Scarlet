package com.example.scarlet.feature_training_log.domain.use_case.session

data class SessionUseCases(
    val getExercisesWithMovementAndSetsBySessionId: GetExercisesWithMovementAndSetsBySessionIdUseCase,
    val getMovementsFilteredByName: GetMovementsFilteredByNameUseCase,
    val updateSession: UpdateSessionUseCase,
    val insertExercise: InsertExerciseUseCase,
    val insertSet: InsertSetUseCase,
    val updateSet: UpdateSetUseCase,
    val deleteSet: DeleteSetUseCase,
    val insertMovement: InsertMovementUseCase,
    val deleteExercise: DeleteExerciseUseCase,
    val updateExercise: UpdateExerciseUseCase,
    val copyPreviousSetField: CopyPreviousSetFieldUseCase,
)