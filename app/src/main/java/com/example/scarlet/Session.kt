package com.example.scarlet

import android.database.Cursor
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import java.io.Serializable

data class Session(
    val id: Long?,
    var blockId: Long?,
    var date: String?) : Serializable {

    constructor(cursor: Cursor) : this(
        cursor.getLongOrNull(cursor.getColumnIndex("id")),
        cursor.getLongOrNull(cursor.getColumnIndex("block_id")),
        cursor.getStringOrNull(cursor.getColumnIndex("date")))
}
