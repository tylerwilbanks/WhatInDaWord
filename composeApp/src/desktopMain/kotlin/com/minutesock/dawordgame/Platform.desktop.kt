package com.minutesock.dawordgame

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import com.minutesock.dawordgame.core.domain.GameLanguage
import java.io.File
import java.util.Locale

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun readFile(filename: String): String {
    return File("src/commonMain/composeResources/files/$filename").readText()
}

actual fun getSystemLanguage(): GameLanguage {
    return GameLanguage.fromSystem(Locale.getDefault().language)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenWidth() = LocalWindowInfo.current.containerSize.width

@Composable
actual fun getScreenOrientation() = ScreenOrientation.Portrait

@Composable
actual fun ToggleSystemStatusBar(hide: Boolean) = Unit

class DesktopSystemUiController : SystemUiController {
    override fun setStatusBarStyles(statusBarColor: Color, navigationBarColor: Color, darkMode: Boolean) = Unit
}