package com.example.scarlet.model

import android.database.Cursor
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import java.io.Serializable

/**
 * Implements a training session
 *
 * @property id Unique database identifier for the block
 * @property blockId Unique database identifier of this session's block
 * @property date Date of the session
 */
data class Session(
    val id: Long?,
    var blockId: Long?,
    var date: String?) : Serializable {

    /**
     * Constructs a session using a result set represented by a [Cursor].
     * Each property is set using the associated column.
     *
     * Note : if a column is missing from the result set, the associated property is set to null
     *
     * @param cursor Result set from a query
     */
    constructor(cursor: Cursor) : this(
        cursor.getLongOrNull(cursor.getColumnIndex("id")),
        cursor.getLongOrNull(cursor.getColumnIndex("block_id")),
        cursor.getStringOrNull(cursor.getColumnIndex("date")))
}
