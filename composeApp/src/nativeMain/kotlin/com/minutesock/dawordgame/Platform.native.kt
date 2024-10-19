package com.minutesock.dawordgame

import com.minutesock.dawordgame.core.domain.GameLanguage

actual fun readFile(filename: String): String {
    TODO("Not yet implemented")
}

actual fun getSystemLanguage(): GameLanguage {
    return GameLanguage.fromSystem(Locale.current.languageCode)
}