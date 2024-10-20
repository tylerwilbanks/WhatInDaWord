package com.minutesock.dawordgame.core.util

import org.jetbrains.compose.resources.StringResource

sealed class Option<T>(
    val data: T? = null,
    val message: String? = null,
    val stringResource: StringResource? = null,
    val errorCode: Int? = null
) {

    class Loading<T>(data: T? = null) : Option<T>(data = data)

    class Success<T>(data: T?) : Option<T>(data = data)

    class Error<T>(
        stringResource: StringResource? = null,
        message: String? = null,
        data: T? = null,
        errorCode: Int? = null
    ) :
        Option<T>(
            data = data,
            stringResource = stringResource,
            message = message,
            errorCode = errorCode
        )

    val hasMessageToDisplay get() = stringResource != null
}