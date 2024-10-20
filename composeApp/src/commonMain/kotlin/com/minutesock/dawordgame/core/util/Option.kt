package com.minutesock.dawordgame.core.util

import com.minutesock.dawordgame.core.uiutil.TextRes

sealed class Option<T>(
    val data: T? = null,
    val message: String? = null,
    val textRes: TextRes? = null,
    val errorCode: Int? = null
) {
    class Loading<T>(data: T? = null) : Option<T>(data = data)
    class Success<T>(data: T?) : Option<T>(data = data)
    class Error<T>(
        textRes: TextRes? = null,
        message: String? = null,
        data: T? = null,
        errorCode: Int? = null
    ) :
        Option<T>(
            data = data,
            textRes = textRes,
            message = message,
            errorCode = errorCode
        )

    val hasMessageToDisplay get() = textRes != null
}