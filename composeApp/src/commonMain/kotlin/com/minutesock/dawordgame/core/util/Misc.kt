package com.minutesock.dawordgame.core.util

fun Int.formatDecimalSeparator(): String {
    return this.toString()
        .reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()
}
