package com.example.scarlet

import android.database.Cursor
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import java.io.Serializable

data class Block(
    val id: Long?,
    var name: String?,
    var completed: Boolean?) : Serializable {

    constructor(cursor: Cursor) : this(
        cursor.getLongOrNull(cursor.getColumnIndex("id")),
        cursor.getStringOrNull(cursor.getColumnIndex("name")),
        cursor.getIntOrNull(cursor.getColumnIndex("completed")) == 1)
}