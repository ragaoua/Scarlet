package com.example.scarlet.core.di

import android.app.Application
import androidx.room.Room
import com.example.scarlet.feature_training_log.data.data_source.ScarletDatabase
import com.example.scarlet.feature_training_log.data.repository.ScarletRepositoryImpl
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.use_case.block.BlockUseCases
import com.example.scarlet.feature_training_log.domain.use_case.block.DeleteSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.GetSessionsWithMovementsByBlockIdUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.InsertSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.block.UpdateBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.DeleteSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.GetAllMovementsUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.GetExercisesWithMovementAndSetsBySessionIdUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertExerciseUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.InsertSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.SessionUseCases
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateSessionUseCase
import com.example.scarlet.feature_training_log.domain.use_case.session.UpdateSetUseCase
import com.example.scarlet.feature_training_log.domain.use_case.training_log.DeleteBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.training_log.GetActiveBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.training_log.GetCompletedBlocksUseCase
import com.example.scarlet.feature_training_log.domain.use_case.training_log.InsertBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.training_log.TrainingLogUseCases
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
    fun provideTrainingLogUseCases(repository: ScarletRepository) =
        TrainingLogUseCases(
            getActiveBlock = GetActiveBlockUseCase(repository),
            getCompletedBlocks = GetCompletedBlocksUseCase(repository),
            deleteBlock = DeleteBlockUseCase(repository),
            insertBlock = InsertBlockUseCase(repository)
        )

    @Provides
    @Singleton
    fun provideBlockUseCases(repository: ScarletRepository) =
        BlockUseCases(
            getSessionsWithMovementsByBlockId = GetSessionsWithMovementsByBlockIdUseCase(repository),
            insertSession = InsertSessionUseCase(repository),
            updateBlock = UpdateBlockUseCase(repository),
            deleteSession = DeleteSessionUseCase(repository)
        )

    @Provides
    @Singleton
    fun provideSessionUseCases(repository: ScarletRepository) =
        SessionUseCases(
            getExercisesWithMovementAndSetsBySessionId =
                GetExercisesWithMovementAndSetsBySessionIdUseCase(repository),
            getAllMovements = GetAllMovementsUseCase(repository),
            updateSession = UpdateSessionUseCase(repository),
            insertExercise = InsertExerciseUseCase(repository),
            insertSet = InsertSetUseCase(repository),
            updateSet = UpdateSetUseCase(repository),
            deleteSet = DeleteSetUseCase(repository)
        )
}