package com.example.scarlet.core.di

import android.app.Application
import androidx.room.Room
import com.example.scarlet.feature_training_log.data.data_source.ScarletDatabase
import com.example.scarlet.feature_training_log.data.repository.ScarletRepositoryImpl
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.use_case.block.BlockUseCases
import com.example.scarlet.feature_training_log.domain.use_case.block.CopyPrecedingSetFieldUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.DeleteExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.DeleteSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.DeleteSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.GetDaysWithSessionsWithMovementAndSetsByBlockIdUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.GetMovementsFilteredByNameUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.InsertExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.InsertMovementUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.InsertSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.InsertSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.UpdateBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.UpdateExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.UpdateLoadBasedOnPreviousSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.UpdateSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.UpdateSetUseCase
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
            copyPrecedingSetField = CopyPrecedingSetFieldUseCase(repository),
            updateLoadBasedOnPreviousSet = UpdateLoadBasedOnPreviousSetUseCase(repository)
        )
}