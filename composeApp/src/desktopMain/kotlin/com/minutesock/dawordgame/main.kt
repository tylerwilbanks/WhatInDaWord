package com.minutesock.dawordgame

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.minutesock.dawordgame.di.initKoin

fun main() {
    application {
        initKoin()
        Window(
            onCloseRequest = ::exitApplication,
            title = "What In Da Word!?",
        ) {
            App()
        }
    }
}