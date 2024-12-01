package com.minutesock.dawordgame

import androidx.compose.ui.window.ComposeUIViewController
import com.minutesock.dawordgame.core.di.iosModule
import com.minutesock.dawordgame.core.di.iosUiModule
import com.minutesock.dawordgame.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin {
            modules(iosModule, iosUiModule)
        }
    }
) {
    App()
}