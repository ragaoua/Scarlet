package com.example.scarlet.core.di

import android.app.Application
import androidx.room.Room
import com.example.scarlet.feature_training_log.data.data_source.ScarletDatabase
import com.example.scarlet.feature_training_log.data.repository.ScarletRepositoryImpl
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.use_case.block.DeleteBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.GetAllBlocksUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.InsertBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.UpdateBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.helpers.ValidateBlockNameHelper
import com.example.scarlet.feature_training_log.domain.use_case.day.GetDaysWithSessionsWithMovementAndSetsByBlockIdUseCase
import com.example.scarlet.feature_training_log.domain.use_case.exercise.DeleteExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.exercise.InsertExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.exercise.UpdateExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.movement.GetMovementsFilteredByNameUseCase
import com.example.scarlet.feature_training_log.domain.use_case.movement.InsertMovementUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.DeleteSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.CopyPrecedingSetFieldUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.DeleteSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.InsertSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.set.UpdateSetUseCase
import com.example.scarlet.feature_training_log.presentation.block.BlockUseCases
import com.example.scarlet.feature_training_log.presentation.training_log.TrainingLogUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideScarletDatabase(app: Application) =
        Room.databaseBuilder(
            app,
            ScarletDatabase::class.java,
            "scarlet.db"
        ).build()

    @Provides
    @Singleton
    fun provideScarletRepository(database: ScarletDatabase): ScarletRepository =
        ScarletRepositoryImpl(database)

    @Provides
    @Singleton
    fun provideValidateBlockNameHelper(repository: ScarletRepository) =
        ValidateBlockNameHelper(repository)

    @Provides
    @Singleton
    fun provideTrainingLogUseCases(
        repository: ScarletRepository,
        validateBlockName: ValidateBlockNameHelper
    ) = TrainingLogUseCases(
            getAllBlocks = GetAllBlocksUseCase(repository),
            deleteBlock = DeleteBlockUseCase(repository),
            insertBlock = InsertBlockUseCase(repository, validateBlockName)
        )

    @Provides
    @Singleton
    fun provideBlockUseCases(
        repository: ScarletRepository,
        validateBlockName: ValidateBlockNameHelper
    ) = BlockUseCases(
            getDaysWithSessionsWithMovementAndSetsByBlockId = GetDaysWithSessionsWithMovementAndSetsByBlockIdUseCase(repository),
            updateBlock = UpdateBlockUseCase(repository, validateBlockName),
            insertSession = InsertSessionUseCase(repository),
            updateSession = UpdateSessionUseCase(repository),
            deleteSession = DeleteSessionUseCase(repository),
            getMovementsFilteredByName = GetMovementsFilteredByNameUseCase(repository),
            insertExercise = InsertExerciseUseCase(repository),
            updateExercise = UpdateExerciseUseCase(repository),
            insertMovement = InsertMovementUseCase(repository),
            deleteExercise = DeleteExerciseUseCase(repository),
            insertSet = InsertSetUseCase(repository),
            updateSet = UpdateSetUseCase(repository),
            deleteSet = DeleteSetUseCase(repository),
            copyPrecedingSetField = CopyPrecedingSetFieldUseCase(repository)
        )
}