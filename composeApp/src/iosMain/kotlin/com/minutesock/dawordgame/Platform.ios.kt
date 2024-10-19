package com.minutesock.dawordgame

import com.minutesock.dawordgame.core.domain.GameLanguage
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun getSystemLanguage(): GameLanguage {
    return GameLanguage.fromSystem(Locale.current.languageCode)
}