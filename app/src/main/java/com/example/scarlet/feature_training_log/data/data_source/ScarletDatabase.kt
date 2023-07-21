package com.example.scarlet.feature_training_log.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.scarlet.feature_training_log.data.data_source.dao.BlockDao
import com.example.scarlet.feature_training_log.data.data_source.dao.ExerciseDao
import com.example.scarlet.feature_training_log.data.data_source.dao.MovementDao
import com.example.scarlet.feature_training_log.data.data_source.dao.SessionDao
import com.example.scarlet.feature_training_log.data.data_source.dao.SetDao
import com.example.scarlet.feature_training_log.data.data_source.entity.BlockEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.ExerciseEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.MovementEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.SessionEntity
import com.example.scarlet.feature_training_log.data.data_source.entity.SetEntity

@Database(
    entities = [BlockEntity::class,
        SessionEntity::class,
        ExerciseEntity::class,
        MovementEntity::class,
        SetEntity::class],
    version = 1)
@TypeConverters(DateConverter::class)
abstract class ScarletDatabase : RoomDatabase() {

    abstract val blockDao: BlockDao
    abstract val sessionDao: SessionDao
    abstract val exerciseDao: ExerciseDao
    abstract val setDao: SetDao
    abstract val movementDao: MovementDao

}
