package com.minutesock.dawordgame

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.minutesock.dawordgame.core.domain.GameLanguage
import java.awt.Desktop
import java.io.File
import java.net.URI
import java.util.Locale

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val type: PlatformType = PlatformType.Desktop
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun getSystemLanguage(): GameLanguage {
    return GameLanguage.fromSystem(Locale.getDefault().language)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenWidth(): Dp {
    return with (LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
}

@Composable
actual fun getScreenOrientation() = ScreenOrientation.Portrait

@Composable
actual fun ToggleSystemStatusBar(hide: Boolean) = Unit

class DesktopSystemUiController : SystemUiController {
    override fun setStatusBarStyles(statusBarColor: Color, navigationBarColor: Color, darkMode: Boolean) = Unit
}

actual fun shareText(text: String) {
    val clipboard = java.awt.Toolkit.getDefaultToolkit().systemClipboard
    val selection = java.awt.datatransfer.StringSelection(text)
    clipboard.setContents(selection, selection)
}

actual fun openWebsite(url: String) {
    if (!Desktop.isDesktopSupported()) {
        println("Failed to open url: $url | Desktop is not supported on this system.")
        return
    }
    Desktop.getDesktop().apply {
        if (!isSupported(Desktop.Action.BROWSE)) {
            println("Failed to open url: $url | Browsing is not supported on this desktop.")
            return
        }
        try {
            browse(URI(url))
        } catch (e: Exception) {
            println("Failed to open url: $url")
            e.printStackTrace()
        }
    }
}