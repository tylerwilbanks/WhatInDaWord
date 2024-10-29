package com.minutesock.dawordgame.core.data

inline fun <T : Any, R> T?.letFromDb(block: (T) -> R): R? {
    return this?.let { value ->
        when (value) {
            "null" -> null
            else -> block(value)
        }
    }
}