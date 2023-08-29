package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.feature_training_log.domain.use_case.session.GetMovementsFilteredByNameUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateExerciseUseCase

data class BlockUseCases(
    val getDaysWithSessionsWithMovementAndSetsByBlockId: GetDaysWithSessionsWithMovementAndSetsByBlockIdUseCase,
    val insertSession: InsertSessionUseCase,
    val updateBlock: UpdateBlockUseCase,
    val deleteSession: DeleteSessionUseCase,
    val getMovementsFilteredByName: GetMovementsFilteredByNameUseCase,
    val insertExercise: InsertExerciseUseCase,
    val updateExercise: UpdateExerciseUseCase
)