package com.example.scarlet.feature_training_log.domain.use_case.movement

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.data.repository.TestRepository
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.use_case.movement.helpers.ValidateMovementNameHelper
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class InsertMovementUseCaseTest {

    private lateinit var repository: ScarletRepository
    private lateinit var validateMovementNameHelper: ValidateMovementNameHelper
    private lateinit var insertMovementUseCase: InsertMovementUseCase

    @Before
    fun setUp() {
        repository = TestRepository()
        validateMovementNameHelper = ValidateMovementNameHelper(repository)
        insertMovementUseCase = InsertMovementUseCase(repository, validateMovementNameHelper)
        runBlocking {
            repository.insertMovement(Movement(name = "Comp Bench Press"))
        }
    }

    @Test
    fun insertingAlreadyExistingMovement_returnsError() = runBlocking {
        val resource = insertMovementUseCase("Comp Bench Press")
        assertEquals(
            resource.error?.resId,
            R.string.error_movement_already_exists
        )
    }

    @Test
    fun insertingBlankMovement_returnsError() = runBlocking {
        val resource = insertMovementUseCase(" ")
        assertEquals(
            resource.error?.resId,
            R.string.error_movement_name_is_empty
        )
    }

    @Test
    fun insertingNewMovement_returnsSuccess() = runBlocking {
        val resource = insertMovementUseCase("Squat")
        assertTrue(resource is Resource.Success)
    }
}