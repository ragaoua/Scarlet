package com.example.scarlet.core.util

import androidx.compose.ui.Modifier

/**
 * Apply a modifier if a condition is true.
 *
 * @param condition The condition to check.
 * @param modifier The modifier to apply if the condition is true.
 *
 * @return The modifier with the condition applied
 */
fun Modifier.conditional(condition : Boolean, modifier : Modifier.() -> Modifier) : Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}