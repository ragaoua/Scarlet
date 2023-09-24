package com.example.scarlet.feature_training_log.presentation.training_log

import com.example.scarlet.feature_training_log.domain.use_case.block.DeleteBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.GetAllBlocksUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.InsertBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.RestoreBlockWithDaysWithSessionsWithExercisesWithSetsUseCase
import com.example.scarlet.feature_training_log.domain.use_case.day.GetDaysWithSessionsWithExercisesWithMovementAndSetsByBlockIdUseCase

data class TrainingLogUseCases(
    val getAllBlocks: GetAllBlocksUseCase,
    val deleteBlock: DeleteBlockUseCase,
    val insertBlock: InsertBlockUseCase,
    val getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockId
        : GetDaysWithSessionsWithExercisesWithMovementAndSetsByBlockIdUseCase,
    val restoreBlockWithDaysWithSessionsWithExercisesWithSets
        : RestoreBlockWithDaysWithSessionsWithExercisesWithSetsUseCase,
)