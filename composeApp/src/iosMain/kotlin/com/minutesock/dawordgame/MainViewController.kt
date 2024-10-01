package com.minutesock.dawordgame

import androidx.compose.ui.window.ComposeUIViewController
import com.minutesock.dawordgame.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}