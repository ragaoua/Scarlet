package com.example.scarlet.core.di

import android.app.Application
import androidx.room.Room
import com.example.scarlet.feature_training_log.data.data_source.ScarletDatabase
import com.example.scarlet.feature_training_log.data.repository.ScarletRepositoryImpl
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.use_case.block.BlockUseCases
import com.example.scarlet.feature_training_log.domain.use_case.block.DeleteSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.GetDaysWithSessionsWithMovementAndSetsByBlockIdUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.InsertSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.UpdateBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.CopyPrecedingSetFieldUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.DeleteExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.DeleteSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.GetExercisesWithMovementAndSetsBySessionIdUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.GetMovementsFilteredByNameUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertMovementUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.SessionUseCases
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateLoadBasedOnPreviousSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.training_log.DeleteBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.training_log.GetAllBlocksUseCase
import com.example.scarlet.feature_training_log.domain.use_case.training_log.InsertBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.training_log.TrainingLogUseCases
import com.example.scarlet.feature_training_log.domain.use_case.training_log.helpers.ValidateBlockNameHelper
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
            insertSession = InsertSessionUseCase(repository),
            updateBlock = UpdateBlockUseCase(repository, validateBlockName),
            deleteSession = DeleteSessionUseCase(repository),
            getMovementsFilteredByName = GetMovementsFilteredByNameUseCase(repository),
            insertExercise = InsertExerciseUseCase(repository),
            updateExercise = UpdateExerciseUseCase(repository),
            insertMovement = InsertMovementUseCase(repository),
            deleteExercise = DeleteExerciseUseCase(repository)
        )

    @Provides
    @Singleton
    fun provideSessionUseCases(repository: ScarletRepository) =
        SessionUseCases(
            getExercisesWithMovementAndSetsBySessionId =
                GetExercisesWithMovementAndSetsBySessionIdUseCase(repository),
            getMovementsFilteredByName = GetMovementsFilteredByNameUseCase(repository),
            updateSession = UpdateSessionUseCase(repository),
            insertExercise = InsertExerciseUseCase(repository),
            insertSet = InsertSetUseCase(repository),
            updateSet = UpdateSetUseCase(repository),
            deleteSet = DeleteSetUseCase(repository),
            insertMovement = InsertMovementUseCase(repository),
            deleteExercise = DeleteExerciseUseCase(repository),
            updateExercise = UpdateExerciseUseCase(repository),
            copyPrecedingSetField = CopyPrecedingSetFieldUseCase(repository),
            updateLoadBasedOnPreviousSet = UpdateLoadBasedOnPreviousSetUseCase(repository)
        )
}