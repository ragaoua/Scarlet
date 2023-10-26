package com.example.scarlet.core.util

fun <T> Iterable<T>.isSortedWith(comparator: Comparator<in T>): Boolean {
    val iterator = iterator()
    if (!iterator.hasNext()) {
        return true
    }

    var prevElement = iterator.next()
    while (iterator.hasNext()) {
        val currentElement = iterator.next()

        if (comparator.compare(prevElement, currentElement) > 0) {
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
    return isSortedWith(compareBy(selector))
}


