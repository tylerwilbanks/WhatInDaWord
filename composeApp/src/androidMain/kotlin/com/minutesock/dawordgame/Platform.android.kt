package com.minutesock.dawordgame

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import com.minutesock.dawordgame.core.domain.GameLanguage
import java.util.Locale

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun readFile(filename: String): String {
    val context = AndroidContextProvider.applicationContext
    return context.assets.open("composeResources/whatindaword.composeapp.generated.resources/files/$filename")
        .bufferedReader().use { it.readText() }
}

actual fun getSystemLanguage(): GameLanguage {
    return GameLanguage.fromSystem(Locale.getDefault().language)
}

@Composable
actual fun getScreenWidth() = LocalConfiguration.current.screenWidthDp

class AndroidSystemUiController(private val activity: ComponentActivity) : SystemUiController {
    override fun setStatusBarStyles(statusBarColor: Color, navigationBarColor: Color, darkMode: Boolean) {
        activity.enableEdgeToEdge(
            statusBarStyle = when (darkMode) {
                true -> SystemBarStyle.dark(statusBarColor.toArgb())
                false -> SystemBarStyle.light(statusBarColor.toArgb(), 0)
            },
            navigationBarStyle = when (darkMode) {
                true -> SystemBarStyle.dark(navigationBarColor.toArgb())
                false -> SystemBarStyle.light(navigationBarColor.toArgb(), 0)
            }
        )
    }
}