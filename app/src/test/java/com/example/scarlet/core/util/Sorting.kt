package com.example.scarlet.core.util

/**
 * Check if an iterable is sorted with a given comparison function.
 * If the iterable is empty, it is considered sorted.
 *
 * @param comparison the comparison function to use for sorting
 *
 * @return true if the iterable is sorted, false otherwise
 */
inline fun <T> Iterable<T>.isSortedWith(crossinline comparison: (T, T) -> Int): Boolean {
    val iterator = iterator()
    if (!iterator.hasNext()) {
        return true
    }

    var prevElement = iterator.next()
    while (iterator.hasNext()) {
        val currentElement = iterator.next()

        if (comparison(prevElement, currentElement) > 0) {
            return false
        }

        prevElement = currentElement
    }

    return true
}

/**
 * Check if an iterable is sorted by a given selector.
 * If the iterable is empty, it is considered sorted.
 * If the iterable contains null values, they are considered greater than any other value.
 *
 * @param selector the selector to use for sorting
 *
 * @return true if the iterable is sorted, false otherwise
 */
inline fun <T, R : Comparable<R>> Iterable<T>.isSortedBy(crossinline selector: (T) -> R?): Boolean {
    val iterator = iterator()
    if (!iterator.hasNext()) {
        return true
    }

    var previousValue = selector(iterator.next())
    while (iterator.hasNext()) {
        val currentValue = selector(iterator.next())

        if(previousValue == null) {
            if (currentValue != null) {
                return false
            }
        } else {
            if (currentValue != null) {
                if (currentValue < previousValue) {
                    return false
                }
            }
        }

        previousValue = currentValue
    }

    return true
}


