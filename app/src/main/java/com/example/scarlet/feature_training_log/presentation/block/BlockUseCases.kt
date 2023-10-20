package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.feature_training_log.domain.use_case.block.UpdateBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.day.GetDaysWithSessionsWithExercisesWithMovementAndSetsByBlockIdUseCase
import com.example.scarlet.feature_training_log.domain.use_case.exercise.DeleteExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.exercise.GetExercisesWithMovementAndSetsBySessionIdUseCase
import com.example.scarlet.feature_training_log.domain.use_case.exercise.InsertExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.exercise.InsertSupersetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.exercise.RestoreExerciseWithMovementAndSetsUseCase
import com.example.scarlet.feature_training_log.domain.use_case.exercise.UpdateExerciseSupersetOrderUseCase
import com.example.scarlet.feature_training_log.domain.use_case.exercise.UpdateExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.exercise.UpdateExercisesOrderUseCase
import com.example.scarlet.feature_training_log.domain.use_case.movement.DeleteMovementUseCase
import com.example.scarlet.feature_training_log.domain.use_case.movement.GetMovementsFilteredByNameUseCase
import com.example.scarlet.feature_training_log.domain.use_case.movement.InsertMovementUseCase
import com.example.scarlet.feature_training_log.domain.use_case.movement.UpdateMovementUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.DeleteSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.RestoreSessionWithExercisesWithMovementAndSetsUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.CopyPrecedingSetFieldUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.DeleteSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.InsertEmptySetWhileSettingsOrderUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.RestoreSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.UpdateSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.ValidateSetFieldValueUseCase

data class BlockUseCases(
    val getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockId:
        GetDaysWithSessionsWithExercisesWithMovementAndSetsByBlockIdUseCase,
    val updateBlock: UpdateBlockUseCase,
    val insertSession: InsertSessionUseCase,
    val updateSession: UpdateSessionUseCase,
    val deleteSession: DeleteSessionUseCase,
    val getMovementsFilteredByName: GetMovementsFilteredByNameUseCase,
    val insertExercise: InsertExerciseUseCase,
    val insertSuperset: InsertSupersetUseCase,
    val updateExercise: UpdateExerciseUseCase,
    val updateExercisesOrder: UpdateExercisesOrderUseCase,
    val updateExerciseSupersetOrder: UpdateExerciseSupersetOrderUseCase,
    val insertMovement: InsertMovementUseCase,
    val updateMovement: UpdateMovementUseCase,
    val deleteMovement: DeleteMovementUseCase,
    val deleteExercise: DeleteExerciseUseCase,
    val restoreExerciseWithMovementAndSets: RestoreExerciseWithMovementAndSetsUseCase,
    val insertEmptySetWhileSettingsOrder: InsertEmptySetWhileSettingsOrderUseCase,
    val restoreSet: RestoreSetUseCase,
    val validateSetFieldValue: ValidateSetFieldValueUseCase,
    val updateSet: UpdateSetUseCase,
    val deleteSet: DeleteSetUseCase,
    val copyPrecedingSetField: CopyPrecedingSetFieldUseCase,
    val getExercisesWithMovementAndSetsBySessionId:
        GetExercisesWithMovementAndSetsBySessionIdUseCase,
    val restoreSessionWithExercisesWithMovementAndSets:
        RestoreSessionWithExercisesWithMovementAndSetsUseCase
)