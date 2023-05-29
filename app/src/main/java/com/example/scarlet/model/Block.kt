package com.example.scarlet.model

import android.database.Cursor
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import java.io.Serializable

/**
 * Implements a training block
 *
 * @property id Unique database identifier for the block
 * @property name Name of the block
 * @property completed Indicates whether the block is completed or not
 */
data class Block(
    val id: Long?,
    var name: String?,
    var completed: Boolean?) : Serializable {

    /**
     * Constructs a block using a result set represented by a [Cursor].
     * Each property is set using the associated column.
     *
     * Note : if a column is missing from the result set, the associated property is set to null
     *
     * @param cursor Result set from a query
     */
    constructor(cursor: Cursor) : this(
        cursor.getLongOrNull(cursor.getColumnIndex("id")),
        cursor.getStringOrNull(cursor.getColumnIndex("name")),
        cursor.getIntOrNull(cursor.getColumnIndex("completed")) == 1)
}