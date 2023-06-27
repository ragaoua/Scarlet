package com.example.scarlet.feature_training_log.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.Set

@Database(
    entities = [Block::class,
        Session::class,
        Exercise::class,
        Movement::class,
        Set::class],
    version = 1)
abstract class ScarletDatabase : RoomDatabase() {

    abstract val blockDao: BlockDao
    abstract val sessionDao: SessionDao
    abstract val exerciseDao: ExerciseDao
    abstract val setDao: SetDao

}
