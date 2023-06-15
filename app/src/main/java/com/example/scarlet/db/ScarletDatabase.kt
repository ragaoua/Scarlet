package com.example.scarlet.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scarlet.db.dao.BlockDao
import com.example.scarlet.db.dao.ExerciseDao
import com.example.scarlet.db.dao.SessionDao
import com.example.scarlet.db.model.Block
import com.example.scarlet.db.model.Exercise
import com.example.scarlet.db.model.Movement
import com.example.scarlet.db.model.Session
import com.example.scarlet.db.model.Set

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

}
