package com.example.scarlet.feature_training_log.domain.use_case.movement

import com.example.scarlet.feature_training_log.data.repository.TestRepository
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMovementsFilteredByNameUseCaseTest {
    private val repository: ScarletRepository = TestRepository()
    private val getMovementsFilteredByNameUseCase = GetMovementsFilteredByNameUseCase(repository)

    init {
        runBlocking {
            repository.insertMovement(Movement(name = "Comp Bench Press"))
            repository.insertMovement(Movement(name = "Comp Squat"))
            repository.insertMovement(Movement(name = "Sumo Deadlift"))
        }
    }

    @Test
    fun emptyFilter_returnsAllMovementsSortedByName() = runBlocking {
        val resource = getMovementsFilteredByNameUseCase("").first()
        assertEquals(
            resource.data?.map { it.name },
            listOf(
                "Comp Bench Press",
                "Comp Squat",
                "Sumo Deadlift"
            ).sorted()
        )
    }

    @Test
    fun filter_returnsCaseInsensitiveMatchingMovementsSortedByName() = runBlocking {
        val resource = getMovementsFilteredByNameUseCase("comp").first()
        assertEquals(
            resource.data?.map { it.name },
            listOf(
                "Comp Bench Press",
                "Comp Squat"
            ).sorted()
        )
    }
}