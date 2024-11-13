package com.minutesock.dawordgame

import androidx.compose.runtime.Composable
import com.minutesock.dawordgame.core.domain.GameLanguage

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun readFile(filename: String): String

expect fun getSystemLanguage(): GameLanguage

@Composable
expect fun getScreenWidth(): Int

interface SystemUiController {
    fun setStatusBarStyles(

    )
}