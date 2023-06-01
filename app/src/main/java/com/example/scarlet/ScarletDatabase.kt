package com.example.scarlet

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.scarlet.dao.BlockDao
import com.example.scarlet.dao.SessionDao
import com.example.scarlet.model.Block
import com.example.scarlet.model.Session

@Database(entities = [Block::class, Session::class], version = 1)
abstract class ScarletDatabase : RoomDatabase() {

    abstract fun blockDao(): BlockDao
    abstract fun sessionDao(): SessionDao

    companion object {
        private var dbInstance: ScarletDatabase? = null

        fun getInstance(context: Context): ScarletDatabase {
            if (dbInstance == null) {
                synchronized(ScarletDatabase::class) {
                    dbInstance = Room.databaseBuilder(context.applicationContext, ScarletDatabase::class.java, "scarlet.db").build()
                }
            }
            return dbInstance!!
        }
    }
}