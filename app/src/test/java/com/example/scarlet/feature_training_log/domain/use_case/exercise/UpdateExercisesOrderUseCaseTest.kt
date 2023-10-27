package com.example.scarlet.feature_training_log.domain.use_case.exercise

import com.example.scarlet.R
import com.example.scarlet.feature_training_log.data.repository.TestRepository
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateExercisesOrderUseCaseTest {

    private val repository: ScarletRepository = TestRepository()
    private val updateExercisesOrderUseCase = UpdateExercisesOrderUseCase(repository)

    init {
        runBlocking {
            repository.insertBlockWithDays(
                block = Block(id = 1),
                days = listOf(Day(id = 1))
            )

            repository.insertSessionWithExercises(
                session = Session(id = 1, dayId = 1),
                exercises = listOf(
                    Exercise(id = 1, sessionId = 1),
                    Exercise(id = 2, sessionId = 1),
                    Exercise(id = 3, sessionId = 1),
                    Exercise(id = 4, sessionId = 1),
                    Exercise(id = 5, sessionId = 1)
                )
            )
        }
    }

    @Test
    fun updateExercisesOrderWhenExercisesDontShareTheSameSessionId_returnsError() = runBlocking {
        val exercises = listOf(
            Exercise(id = 1, sessionId = 1),
            Exercise(id = 2, sessionId = 2),
            Exercise(id = 3, sessionId = 3),
            Exercise(id = 4, sessionId = 4),
            Exercise(id = 5, sessionId = 5)
        )
        val error = updateExercisesOrderUseCase(exercises, 1).error
        assertTrue(error?.resId == R.string.error_exercises_must_share_the_same_session_id)
    }

    @Test
    fun updateExercisesOrderWhenExercisesDontShareTheSameOrder_returnsError() = runBlocking {
        val exercises = listOf(
            Exercise(id = 1, sessionId = 1, order = 1),
            Exercise(id = 2, sessionId = 1, order = 2),
            Exercise(id = 3, sessionId = 1, order = 3),
            Exercise(id = 4, sessionId = 1, order = 4),
            Exercise(id = 5, sessionId = 1, order = 5)
        )
        val error = updateExercisesOrderUseCase(exercises, 1).error
        assertTrue(error?.resId == R.string.error_exercises_must_share_the_same_order)
    }
}