package com.minutesock.dawordgame

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import com.minutesock.dawordgame.core.domain.GameLanguage
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRect
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.stringWithString
import platform.UIKit.UIActivityViewController
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

actual fun getSystemLanguage(): GameLanguage {
    return GameLanguage.English
//    return GameLanguage.fromSystem(Locale.current.language)
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

actual fun shareText(text: String) {
    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    val activityViewController = UIActivityViewController(
        activityItems = listOf(NSString.stringWithString(text)),
        applicationActivities = null
    )
    rootViewController?.presentViewController(
        activityViewController,
        animated = true,
        completion = null
    )
}

actual fun openWebsite(url: String) {
    NSURL.URLWithString(url)?.let { nsUrl ->
        UIApplication.sharedApplication.openURL(nsUrl)
    }
}