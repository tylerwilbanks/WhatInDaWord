package com.minutesock.dawordgame.core.uiutil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import com.minutesock.dawordgame.getScreenWidth

enum class WindowSizeBreakpoint {
    ExtraSmall,
    Small,
    Medium,
    Large,
    ExtraLarge;

    companion object {
        fun fromDp(width: Dp): WindowSizeBreakpoint {
            return when {
                width.value < 600f -> ExtraSmall
                width.value in 600f..904f -> Small
                width.value in 905f..1239f -> Medium
                width.value in 1240f..1439f -> Large
                width.value >= 1440f -> ExtraLarge
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
            WindowSizeBreakpoint.fromDp(windowWidth)
        }
    }
}