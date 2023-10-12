package com.example.scarlet.core.util

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * @return true if the scroll state is scrolling down.
 */
@Composable
fun ScrollState.isScrollingDown(): Boolean {
    var previousScrollValue by remember(this) { mutableIntStateOf(value) }
    return remember(this) {
        derivedStateOf {
            let {
                previousScrollValue < value
            }.also {
                previousScrollValue = value
            }
        }
    }.value
}