package com.example.scarlet.core.util

import kotlin.math.roundToInt

fun Float.roundToClosestMultipleOf(multiple: Float): Float {
    return (this / multiple).roundToInt() * multiple
}