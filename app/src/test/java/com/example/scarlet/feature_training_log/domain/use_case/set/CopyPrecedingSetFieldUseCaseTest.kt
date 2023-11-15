package com.example.scarlet.feature_training_log.domain.use_case.set

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.data.repository.TestRepository
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.presentation.block.util.SetFieldType
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CopyPrecedingSetFieldUseCaseTest {

    private lateinit var repository: ScarletRepository
    private lateinit var copyPrecedingSetField: CopyPrecedingSetFieldUseCase

    @Before
    fun setUp() {
        repository = TestRepository()
        copyPrecedingSetField = CopyPrecedingSetFieldUseCase(repository)
        runBlocking {
            repository.insertSetWhileSettingOrder(
                Set(
                    id = 1,
                    exerciseId = 1,
                    reps = 10,
                    load = 100f,
                    rating = 5f
                )
            )
            repository.insertSetWhileSettingOrder(
                Set(
                    id = 1,
                    exerciseId = 1,
                    reps = 7,
                    load = 120f,
                    rating = 8f
                )
            )
        }
    }

    @Test
    fun noPreviousSet_returnsError() = runBlocking {
        val set = Set(
            id = 1,
            exerciseId = 1,
            order = 1,
            reps = 10,
            load = 100f,
            rating = 5f
        )
        val sessionExercises = listOf(
            ExerciseWithMovementAndSets(
                id = 1,
                movementId = 1,
                order = 1,
                movement = Movement(),
                sets = listOf(set)
            )
        )
        val fieldToCopy = SetFieldType.REPS

        val resource = copyPrecedingSetField(set, sessionExercises, fieldToCopy)

        assertEquals(
            resource.error?.resId,
            R.string.error_no_previous_set
        )
    }

    @Test
    fun copyPreviousSet_returnsSuccess() = runBlocking {
        val set1 = Set(
            id = 1,
            exerciseId = 1,
            order = 1,
            reps = 10,
            load = 100f,
            rating = 5f
        )
        val set2 = Set(
            id = 1,
            exerciseId = 1,
            order = 2,
            reps = 7,
            load = 120f,
            rating = 8f
        )
        val sessionExercises = listOf(
            ExerciseWithMovementAndSets(
                id = 1,
                movementId = 1,
                order = 1,
                movement = Movement(),
                sets = listOf(set1, set2)
            )
        )
        val fieldToCopy = SetFieldType.REPS

        val resource = copyPrecedingSetField(set2, sessionExercises, fieldToCopy)

        assertTrue(resource is Resource.Success)
    }

}