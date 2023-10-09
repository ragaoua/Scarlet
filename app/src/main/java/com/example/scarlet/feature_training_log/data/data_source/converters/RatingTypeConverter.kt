package com.example.scarlet.feature_training_log.data.data_source.converters

import androidx.room.TypeConverter
import com.example.scarlet.feature_training_log.domain.model.RatingType

class RatingTypeConverter {

    @TypeConverter
    fun ratingTypeToString(ratingType: RatingType): String {
        return ratingType.name
    }

    @TypeConverter
    fun stringToRatingType(ratingType: String): RatingType {
        return RatingType.valueOf(ratingType)
    }
}