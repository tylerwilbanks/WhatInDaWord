package com.minutesock.dawordgame

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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

@Composable
actual fun getScreenOrientation(): ScreenOrientation {
    return when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> ScreenOrientation.Portrait
        Configuration.ORIENTATION_LANDSCAPE -> ScreenOrientation.Landscape
        else -> ScreenOrientation.Portrait
    }
}

@Composable
actual fun ToggleSystemStatusBar(hide: Boolean) {
    val activity = LocalContext.current as? Activity ?: return
    LaunchedEffect(hide) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = activity.window?.insetsController
            if (hide) {
                windowInsetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                windowInsetsController?.show(WindowInsets.Type.statusBars())
            }
        } else {
            if (hide) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            } else {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }
}

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