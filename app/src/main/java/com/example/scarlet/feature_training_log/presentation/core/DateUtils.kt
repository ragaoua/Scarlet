package com.example.scarlet.feature_training_log.presentation.core

import android.icu.text.DateFormat.getDateInstance
import java.util.Date

object DateUtils {
    fun formatDate(date: Date): String {
        return getDateInstance().format(date)
    }
}