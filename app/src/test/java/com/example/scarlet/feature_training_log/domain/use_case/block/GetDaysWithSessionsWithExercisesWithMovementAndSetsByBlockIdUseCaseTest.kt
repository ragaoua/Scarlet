package com.example.scarlet.feature_training_log.domain.use_case.block

import com.example.scarlet.core.util.isSortedBy
import com.example.scarlet.core.util.isSortedWith
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.repository.TestRepository
import com.example.scarlet.feature_training_log.domain.use_case.day.GetDaysWithSessionsWithExercisesWithMovementAndSetsByBlockIdUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.lang.Math.random
import java.util.Date

/**
 * Insert 10 movements, and 1 block with 4 days and 50 sessions with random dates.
 * For each session, insert 5 exercises and 1 superset
 * For each exercise, insert 3 sets.
 * Some blocks may have no sessions.
 * SOme sessions in each day share the same date.
 * Some movements may have no exercises.
 *
 * Check if the days are returned correctly sorted by order.
 * Check if the sessions are returned correctly sorted by date then by id.
 * Check if the exercises are returned correctly sorted by order then by supersetOrder.
 * Check if the sets are returned correctly sorted by order.
 *
 * @see GetDaysWithSessionsWithExercisesWithMovementAndSetsByBlockIdUseCase
 */
class GetDaysWithSessionsWithExercisesWithMovementAndSetsByBlockIdUseCaseTest {

    private val repository: ScarletRepository = TestRepository()
    private var getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockId =
        GetDaysWithSessionsWithExercisesWithMovementAndSetsByBlockIdUseCase(repository)
    private val BLOCK_ID = 1L

    @Before
    fun setUp() = runBlocking {
        // Insert movements
        val nbMovementsInserted = 10
        (1L..nbMovementsInserted).forEach {
            repository.insertMovement(
                Movement(id = it) // The movement's name is irrelevant here
            )
        }

        // Insert blocks and days
        val nbDaysInserted = 4
        repository.insertBlockWithDays(
            block = Block(id = BLOCK_ID),
            days = (1L..nbDaysInserted).map { Day(id = it) }
        )

        // Insert sessions and exercises
        (1L..50L).forEach { sessionId ->
            // Insert a session

            // Randomly assign a date but limit the choice to 5 possibilities.
            // Given the number of sessions (50) and days (4) inserted, at least 1 day
            // will have 2 sessions with the same date, which is what we want to test
            // (if 2 sessions have the same date, they should be sorted by id)
            val date = Date((random() * 4).toLong()*3600000)
            val session = Session(
                id = sessionId,
                date = date,
                dayId = (random() * (nbDaysInserted-1)).toLong() + 1 // Randomly assign a day
            )
            repository.insertSessionWithExercises(session, emptyList())

            // Insert exercises and sets
            val nbExercisesInserted = 5
            val nbSupersetExercisesInserted = 3
            (1..nbExercisesInserted).forEach {
                // Insert exercise
                val exerciseId = it + ((nbExercisesInserted+nbSupersetExercisesInserted) * (sessionId-1))
                val exercise = Exercise(
                    id = exerciseId,
                    sessionId = sessionId,
                    movementId = (random() * (nbMovementsInserted-1)).toLong() + 1 // Randomly assign a movement
                )
                repository.insertExercisesWhileSettingOrder(listOf(exercise))

                // Insert sets
                (1..3).forEach { _ ->
                    val set = Set(exerciseId = exerciseId)

                    repository.insertSetWhileSettingOrder(set)
                }
            }

            // Insert superset
            val supersetExercises = (1..nbSupersetExercisesInserted).map {
                Exercise(
                    id = it + ((nbExercisesInserted+nbSupersetExercisesInserted) * (sessionId-1)) + nbExercisesInserted,
                    sessionId = sessionId,
                    movementId = (random() * (nbMovementsInserted-1)).toLong() + 1 // Randomly assign a movement
                )
            }
            repository.insertExercisesWhileSettingOrder(supersetExercises)

            supersetExercises.forEach {
                val set = Set(exerciseId = it.id)
                repository.insertSetWhileSettingOrder(set)
            }
        }
    }

    @Test
    fun getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockId_returnsCorrectlySortedData() = runBlocking {
        getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockId(BLOCK_ID).first().data?.let { days ->
            if (!days.isSortedBy { it.order }) {
                fail("Days are not sorted by order")
            }

            days.forEach { day ->
                val areSessionsSortedByDateThenId = day.sessions.isSortedWith { s1, s2 ->
                    s1.date.compareTo(s2.date)
                        .takeIf { it != 0 }
                        ?: s1.id.compareTo(s2.id)
                }
                if (!areSessionsSortedByDateThenId) {
                    fail("Session are not sorted by date then by id")
                }

                day.sessions.forEach { session ->
                    val areExercisesSortedByOrderThenSupersetOrder = session.exercises.isSortedWith { e1, e2 ->
                        e1.order.compareTo(e2.order)
                            .takeIf { it != 0 }
                            ?: e1.supersetOrder.compareTo(e2.supersetOrder)
                    }
                    if (!areExercisesSortedByOrderThenSupersetOrder) {
                        fail("Exercises are not sorted by order then by supersetOrder")
                    }

                    session.exercises.forEach { exercise ->
                        if (!exercise.sets.isSortedBy { it.order }) {
                            fail("Sets are not sorted by order")
                        }
                    }
                }
            }
        } ?: fail("Data is null")
    }
}