package com.minutesock.dawordgame.core.util

import com.minutesock.dawordgame.core.uiutil.TextRes

sealed class Option<out T> {
    class Loading(
        val textRes: TextRes,
    ) : Option<Nothing>()

    class Error(
        val textRes: TextRes,
        val errorCode: Int = 1_000
    ) : Option<Nothing>()

    class Success<out T>(val data: T) : Option<T>()
}

