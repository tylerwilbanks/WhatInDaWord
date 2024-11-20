package com.minutesock.dawordgame.core.uiutil

import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed class TextRes {
    data class Raw(
        val value: String
    ) : TextRes()

    data class StringRes(
        val resource: StringResource,
        val args: ImmutableList<Any>? = null
    ) : TextRes()

    fun asRawString(): String {
        return when (this) {
            is Raw -> value
            is StringRes -> "Failed to retrieve string resource: ${resource.key}"
        }
    }

    @Composable
    fun asString(): String {
        return when (this) {
            is Raw -> value
            is StringRes -> {
                if (args == null) {
                    stringResource(resource)
                } else {
                    stringResource(resource, args)
                }
            }
        }
    }

    fun empty() = Raw("")
}