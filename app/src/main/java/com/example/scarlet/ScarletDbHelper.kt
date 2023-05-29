package com.example.scarlet

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

private const val TAG = "ScarletDbHelper"

class ScarletDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private lateinit var db: SQLiteDatabase

    override fun onCreate(db: SQLiteDatabase) {
        this.db = db
        Log.d(TAG, "Creating the database schema")

        createBlockTable()
        createSessionTable()
    }

    /**
     * Executes SQL to create the Block table
     */
    private fun createBlockTable() {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS block (
                id INTEGER PRIMARY KEY,
                name TEXT,
                completed INTEGER
            )
        """
        db.execSQL(createTableQuery)
    }

    /**
     * Executes SQL to create the Session table
     */
    private fun createSessionTable() {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS session (
                id INTEGER PRIMARY KEY,
                block_id INTEGER,
                date TEXT,
                FOREIGN KEY (block_id) REFERENCES block(id)
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "Dropping and recreating the database schema")
        db!!.execSQL(DROP_SCHEMA_QUERY)
        this.onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "scarlet"
        private const val DATABASE_VERSION = 1
        private const val DROP_SCHEMA_QUERY = "DROP TABLE IF EXISTS block"
    }
}