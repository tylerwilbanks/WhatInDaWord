package com.minutesock.dawordgame.core.uiutil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.datetime.Clock

@Composable
fun debouncedAction(debounceTime: Long = 2_000, action: () -> Unit): () -> Unit {
    var lastClickTime by mutableStateOf(0L)

    return remember(lastClickTime) {
        {
            val currentTime = Clock.System.now().toEpochMilliseconds()
            if (currentTime - lastClickTime >= debounceTime) {
                action()
                lastClickTime = currentTime
            }
        }
    }
}