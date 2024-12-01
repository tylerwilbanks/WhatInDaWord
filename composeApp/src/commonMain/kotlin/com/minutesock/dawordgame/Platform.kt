package com.minutesock.dawordgame

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.minutesock.dawordgame.core.domain.GameLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

enum class PlatformType {
    Android,
    Desktop,
    Native
}

interface Platform {
    val name: String
    val type: PlatformType
}

expect fun getPlatform(): Platform

expect fun getSystemLanguage(): GameLanguage

@Composable
expect fun getScreenWidth(): Dp

@Composable
expect fun getScreenOrientation(): ScreenOrientation

@Composable
expect fun ToggleSystemStatusBar(hide: Boolean)

enum class ScreenOrientation {
    Portrait,
    Landscape;
}

interface SystemUiController {
    fun setStatusBarStyles(
        statusBarColor: Color,
        navigationBarColor: Color,
        darkMode: Boolean
    )
}

class StatefulSystemUiController(
    private val platformSystemUiController: SystemUiController,
    statusBarColor: Color = Color.Transparent,
    navigationBarColor: Color = Color.Transparent,
    darkMode: Boolean = true
) {
    private val _statusBarColor = MutableStateFlow(statusBarColor)
    private val _navigationBarColor = MutableStateFlow(navigationBarColor)
    private val _darkMode = MutableStateFlow(darkMode)

    fun setStatusBarStyles(
        statusBarColor: Color = _statusBarColor.value,
        navigationBarColor: Color = _navigationBarColor.value,
        darkMode: Boolean = _darkMode.value
    ) {
        _statusBarColor.update { statusBarColor }
        _navigationBarColor.update { navigationBarColor }
        _darkMode.update { darkMode }
        platformSystemUiController.setStatusBarStyles(_statusBarColor.value, _navigationBarColor.value, _darkMode.value)
    }
}

expect fun shareText(text: String)

expect fun openWebsite(url: String)