package com.example.scarlet.feature_training_log.data.data_source.converters

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun dateToLong(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun longToDate(timestamp: Long): Date {
        return Date(timestamp)
    }
}
