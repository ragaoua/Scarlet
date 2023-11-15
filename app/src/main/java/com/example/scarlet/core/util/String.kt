package com.example.scarlet.core.util

import java.util.Locale

fun String.toTitleCase(): String {
    return split(" ").joinToString(" ") { word ->
        word.lowercase().replaceFirstChar {
            it.titlecase(Locale.ROOT)
        }
    }
}