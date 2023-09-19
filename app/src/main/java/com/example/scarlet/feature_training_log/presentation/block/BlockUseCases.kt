package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.feature_training_log.domain.use_case.block.UpdateBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.day.GetDaysWithSessionsWithMovementAndSetsByBlockIdUseCase
import com.example.scarlet.feature_training_log.domain.use_case.exercise.DeleteExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.exercise.InsertExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.exercise.UpdateExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.movement.GetMovementsFilteredByNameUseCase
import com.example.scarlet.feature_training_log.domain.use_case.movement.InsertMovementUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.DeleteSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.CopyPrecedingSetFieldUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.DeleteSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.InsertSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.UpdateLoadBasedOnPreviousSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.UpdateSetUseCase

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