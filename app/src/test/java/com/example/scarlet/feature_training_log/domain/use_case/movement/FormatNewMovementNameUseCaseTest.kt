package com.example.scarlet.feature_training_log.domain.use_case.movement

import com.example.scarlet.feature_training_log.data.repository.TestRepository
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FormatNewMovementNameUseCaseTest {
    private val repository: ScarletRepository = TestRepository()
    private val formatNewMovementNameUseCase = FormatNewMovementNameUseCase(repository)

    @Test
    fun blancName_returnsNull() = runBlocking {
        val result = formatNewMovementNameUseCase("", listOf())
        assertNull(result.data)
    }

    @Test
    fun existingName_returnsNull() = runBlocking {
        val result = formatNewMovementNameUseCase(
            name = "bENch pRess",
            movements = listOf(
                Movement(name = "Bench Press")
            )
        )
        assertNull(result.data)
    }

    @Test
    fun validName_returnsFormattedName() = runBlocking {
        val result = formatNewMovementNameUseCase(
            name = "bENch pRess",
            movements = listOf(
                Movement(name = "Squat"),
                Movement(name = "Deadlift")
            )
        )
        assertEquals("Bench Press", result.data)
    }
}