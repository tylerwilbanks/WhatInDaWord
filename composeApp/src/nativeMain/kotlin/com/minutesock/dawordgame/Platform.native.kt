package com.minutesock.dawordgame

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.intl.Locale
import com.minutesock.dawordgame.core.domain.GameLanguage

actual fun readFile(filename: String): String {
    TODO("Not yet implemented")
}

actual fun getSystemLanguage(): GameLanguage {
    return GameLanguage.fromSystem(Locale.current.language)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenWidth() = LocalWindowInfo.current.containerSize.width