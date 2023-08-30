package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.feature_training_log.domain.use_case.session.DeleteExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.GetMovementsFilteredByNameUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertMovementUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateSetUseCase

data class BlockUseCases(
    val getDaysWithSessionsWithMovementAndSetsByBlockId: GetDaysWithSessionsWithMovementAndSetsByBlockIdUseCase,
    val insertSession: InsertSessionUseCase,
    val updateBlock: UpdateBlockUseCase,
    val deleteSession: DeleteSessionUseCase,
    val getMovementsFilteredByName: GetMovementsFilteredByNameUseCase,
    val insertExercise: InsertExerciseUseCase,
    val updateExercise: UpdateExerciseUseCase,
    val insertMovement: InsertMovementUseCase,
    val deleteExercise: DeleteExerciseUseCase,
    val insertSet: InsertSetUseCase,
    val updateSet: UpdateSetUseCase
)