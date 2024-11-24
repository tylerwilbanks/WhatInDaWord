package com.minutesock.dawordgame

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.intl.Locale
import com.minutesock.dawordgame.core.domain.GameLanguage

class NativePlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val type: PlatformType = PlatformType.Native
}

actual fun getPlatform(): Platform = NativePlatform()

actual fun readFile(filename: String): String {
    TODO("Not yet implemented")
}

actual fun getSystemLanguage(): GameLanguage {
    return GameLanguage.fromSystem(Locale.current.language)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenWidth() = LocalWindowInfo.current.containerSize.width

@Composable
actual fun ToggleSystemStatusBar(hide: Boolean) = Unit

@Composable
actual fun getScreenOrientation(): ScreenOrientation {
    val screenSize = UIScreen.mainScreen.bounds
    return if (screenSize.width > screenSize.height) {
        ScreenOrientation.Landscape
    } else {
        ScreenOrientation.Portrait
    }
}

class NativeSystemUiController : SystemUiController {
    override fun setStatusBarStyles(statusBarColor: Color, navigationBarColor: Color, darkMode: Boolean) {
        val style =
            if (darkMode) UIStatusBarStyle.UIStatusBarStyleDarkContent else UIStatusBarStyle.UIStatusBarStyleLightContent
        UIApplication.sharedApplication.setStatusBarStyle(style, animated = true)
    }
}

actual fun shareText(text: String) = Unit