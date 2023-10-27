package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.feature_training_log.data.repository.TestRepository
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ISession
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.Math.random
import java.util.Date

/**
 *
 */
class InsertSessionUseCaseTest {

    private val repository: ScarletRepository = TestRepository()
    private var insertSession = InsertSessionUseCase(repository)
    private val BLOCK_ID = 1L

    init {
        runBlocking {
            val nbMovements = 10
            val nbDays = 2
            val nbSessions = 50
            val nbExercisesPerSession = 5
            val nbSupersetExercisesPerSession = 3

            // Insert movements
            (1L..nbMovements).forEach {
                repository.insertMovement(
                    Movement(id = it) // The movement's name is irrelevant here
                )
            }

            // Insert block and days
            repository.insertBlockWithDays(
                block = Block(id = BLOCK_ID),
                days = (1L..nbDays).map { Day(id = it) }
            )

            // Insert sessions and exercises
            (1L..nbSessions).forEach { sessionId ->
                // Insert a session

                // Randomly assign a date but limit the choices to 5 possibilities.
                // Given the number of sessions (50) and days (4) inserted, at least 1 day
                // will have 2 sessions with the same date, which is what we want to test
                // (if 2 sessions have the same date, they should be sorted by id)
                val date = Date((random() * 4).toLong() * 3600000)
                val session = Session(
                    id = sessionId,
                    date = date,
                    dayId = 1 // Only insert sessions for the first day. The second day will have no sessions.
                )
                repository.insertSessionWithExercises(session, emptyList())

                // Insert exercises
                (1..nbExercisesPerSession).forEach {
                    // Insert exercise
                    val exerciseId =
                        it + ((nbExercisesPerSession + nbSupersetExercisesPerSession) * (sessionId - 1))
                    val exercise = Exercise(
                        id = exerciseId,
                        sessionId = sessionId,
                        movementId = (random() * (nbMovements - 1)).toLong() + 1 // Randomly assign a movement
                    )
                    repository.insertExercisesWhileSettingOrder(listOf(exercise))
                }

                // Insert superset
                val supersetExercises = (1..nbSupersetExercisesPerSession).map {
                    Exercise(
                        id = it + ((nbExercisesPerSession + nbSupersetExercisesPerSession) * (sessionId - 1)) + nbExercisesPerSession,
                        sessionId = sessionId,
                        movementId = (random() * (nbMovements - 1)).toLong() + 1 // Randomly assign a movement
                    )
                }
                repository.insertExercisesWhileSettingOrder(supersetExercises)
            }
        }
    }

    @Test
    fun insertSessionWhenThereAreSessionsForThatDay_insertsSessionWithExerciseMovementsFromPrecedingSession() =
        runBlocking {
            val latestSessionExercises = repository.getSessionsWithExercisesByDayId(1)
                .sortedWith(
                    compareByDescending<ISession> { it.date }.thenByDescending { it.id }
                ).first()
                .exercises

            val insertSessionId = insertSession(dayId = 1)

            val insertedSessionExercises = repository.getSessionsWithExercisesByDayId(1)
                .find { it.id == insertSessionId }
                ?.exercises

            assertTrue(
                insertedSessionExercises?.size == latestSessionExercises.size &&
                    // The id and sessionId are set to 0 to compare
                    // only the orders, superset orders, movement ids and rating types
                    insertedSessionExercises.map { it.copy(id = 0, sessionId = 0) }
                        .containsAll(latestSessionExercises.map { it.copy(id = 0, sessionId = 0) })
            )
        }

    @Test
    fun insertSessionWhenThereAreNoSessionsForThatDay_insertsEmptySession() = runBlocking {
        val sessionId = insertSession(dayId = 2)

        val session = repository.getSessionsWithExercisesByDayId(2)
            .find { it.id == sessionId }
        assertTrue(session?.exercises?.isEmpty() ?: false)
    }
}