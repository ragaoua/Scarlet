package com.example.scarlet.di

import android.app.Application
import androidx.room.Room
import com.example.scarlet.db.ScarletDatabase
import com.example.scarlet.db.ScarletRepository
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
//    fun provideScarletDatabase(@ApplicationContext app: Context) =
    fun provideScarletDatabase(app: Application) =
        Room.databaseBuilder(
            app,
            ScarletDatabase::class.java,
            "scarlet.db"
        ).build()

    @Provides
    @Singleton
    fun provideScarletRepository(database: ScarletDatabase) =
        ScarletRepository(database)
}