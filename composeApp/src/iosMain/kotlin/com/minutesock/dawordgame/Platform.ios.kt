package com.minutesock.dawordgame

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.Dp
import com.minutesock.dawordgame.core.domain.GameLanguage
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import okio.FileNotFoundException
import okio.IOException
import platform.CoreGraphics.CGRect
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val type: PlatformType = PlatformType.Native
}

actual fun getPlatform(): Platform = IOSPlatform()

@OptIn(ExperimentalForeignApi::class)
actual fun readFile(filename: String): String {
    val bundle = NSBundle.mainBundle
    val path = bundle.pathForResource(name = filename, ofType = null) ?: throw FileNotFoundException("file not found: $filename")
    val content = NSString.stringWithContentsOfFile(
        path = path,
        encoding = NSUTF8StringEncoding,
        error = null
    )
    return content ?: throw IOException("failed to read file: $filename")
}

actual fun getSystemLanguage(): GameLanguage {
    return GameLanguage.fromSystem(Locale.current.language)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenWidth(): Dp {
    return with (LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
}

@Composable
actual fun ToggleSystemStatusBar(hide: Boolean) = Unit

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun getScreenOrientation(): ScreenOrientation {
    val bounds: CValue<CGRect> = UIScreen.mainScreen.bounds
    val width = bounds.useContents { size.width }
    val height = bounds.useContents { size.height }

    return if (width > height) {
        ScreenOrientation.Landscape
    } else {
        ScreenOrientation.Portrait
    }
}

class IosSystemUiController : SystemUiController {
    override fun setStatusBarStyles(statusBarColor: Color, navigationBarColor: Color, darkMode: Boolean) {
        val style =
            if (darkMode) UIStatusBarStyleDarkContent else UIStatusBarStyleLightContent
        UIApplication.sharedApplication.setStatusBarStyle(style, animated = true)
    }
}

actual fun shareText(text: String) = Unit
actual fun openWebsite(url: String) = Unit