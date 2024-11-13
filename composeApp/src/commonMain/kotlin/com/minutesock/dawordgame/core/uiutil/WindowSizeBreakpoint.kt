package com.minutesock.dawordgame.core.uiutil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.minutesock.dawordgame.getScreenWidth

enum class WindowSizeBreakpoint {
    ExtraSmall,
    Small,
    Medium,
    Large,
    ExtraLarge;

    companion object {
        fun fromPx(widthPx: Int): WindowSizeBreakpoint {
            return when {
                widthPx < 600 -> ExtraSmall
                widthPx in 600..904 -> Small
                widthPx in 905..1239 -> Medium
                widthPx in 1240..1439 -> Large
                widthPx >= 1440 -> ExtraLarge
                else -> ExtraLarge
            }
        }
    }
}

@Composable
fun rememberWindowSizeBreakpoint(): State<WindowSizeBreakpoint> {
    val windowWidth = getScreenWidth()

    return remember(windowWidth) {
        derivedStateOf {
            WindowSizeBreakpoint.fromPx(windowWidth)
        }
    }
}