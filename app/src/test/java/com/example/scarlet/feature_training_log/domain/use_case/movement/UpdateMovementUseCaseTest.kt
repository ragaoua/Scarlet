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

class UpdateMovementUseCaseTest {

    private lateinit var repository: ScarletRepository
    private lateinit var validateMovementNameHelper: ValidateMovementNameHelper
    private lateinit var updateMovement: UpdateMovementUseCase

    @Before
    fun setUp() {
        repository = TestRepository()
        validateMovementNameHelper = ValidateMovementNameHelper(repository)
        updateMovement = UpdateMovementUseCase(repository, validateMovementNameHelper)
        runBlocking {
            repository.insertMovement(Movement(
                id = 1,
                name = "Comp Bench Press"
            ))
            repository.insertMovement(Movement(
                id = 2,
                name = "Comp Squat"
            ))
        }
    }

    @Test
    fun updatingBlankMovement_returnsError() = runBlocking {
        val resource = updateMovement(Movement(
            id = 1,
            name = " "
        ))
        assertEquals(
            resource.error?.resId,
            R.string.error_movement_name_is_empty
        )
    }

    @Test
    fun updatingAlreadyExistingMovement_returnsError() = runBlocking {
        val resource = updateMovement(Movement(
            id = 1,
            name = "Comp Squat"
        ))
        assertEquals(
            resource.error?.resId,
            R.string.error_movement_already_exists
        )
    }

    @Test
    fun updatingMovementWithSameName_returnsSuccess() = runBlocking {
        val resource = updateMovement(Movement(
            id = 1,
            name = "Comp Bench Press"
        ))
        assertTrue(resource is Resource.Success)
    }

    @Test
    fun updatingNewMovementName_returnsSuccess() = runBlocking {
        val resource = updateMovement(Movement(
            id = 1,
            name = "Deadlift"
        ))
        assertTrue(resource is Resource.Success)
    }
}