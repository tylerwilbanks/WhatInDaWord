package com.minutesock.dawordgame

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.minutesock.dawordgame.di.desktopModule
import com.minutesock.dawordgame.di.initKoin

fun main() {
    application {
        initKoin {
            modules(desktopModule)
        }

        Window(
            onCloseRequest = ::exitApplication,
            title = "What In Da Word!?",
        ) {
            App()
        }
    }
}