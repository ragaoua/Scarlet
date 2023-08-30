package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.feature_training_log.domain.use_case.session.CopyPrecedingSetFieldUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.DeleteExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.DeleteSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.GetMovementsFilteredByNameUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertMovementUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateLoadBasedOnPreviousSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateSetUseCase

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