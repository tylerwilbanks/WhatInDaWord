package com.minutesock.dawordgame

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.minutesock.dawordgame.di.KoinProvider
import com.minutesock.dawordgame.di.desktopModule
import com.minutesock.dawordgame.di.initKoin
import io.ktor.client.HttpClient

fun main() {
    application {
        initKoin {
            modules(desktopModule)
        }

        Window(
            onCloseRequest = {
                KoinProvider.instance.get<HttpClient>().close()
                exitApplication()
            },
            title = "What In da word!?",
            state = WindowState(
                size = DpSize(1080.dp, 720.dp)
            )
        ) {
            App()
        }
    }
}