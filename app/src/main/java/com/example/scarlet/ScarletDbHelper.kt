package com.example.scarlet

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

private const val TAG = "ScarletDbHelper"
class ScarletDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private var db: SQLiteDatabase? = null

    override fun onCreate(db: SQLiteDatabase) {
        this.db = db
        Log.d(TAG, "Creating the database schema")
        db.execSQL(CREATE_SCHEMA_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "Dropping and recreating the database schema")
        db!!.execSQL(DROP_SCHEMA_QUERY)
        this.onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "scarlet"
        private const val DATABASE_VERSION = 1
        private const val CREATE_SCHEMA_QUERY = "CREATE TABLE block(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, completed BOOLEAN DEFAULT 0)"
        private const val DROP_SCHEMA_QUERY = "DROP TABLE IF EXISTS block"
    }
}