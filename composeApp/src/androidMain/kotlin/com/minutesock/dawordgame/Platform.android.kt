package com.minutesock.dawordgame

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
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
import androidx.compose.ui.unit.dp
import com.minutesock.dawordgame.core.domain.GameLanguage

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val type: PlatformType = PlatformType.Android
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun getSystemLanguage(): GameLanguage {
    return GameLanguage.English
//    return GameLanguage.fromSystem(Locale.getDefault().language)
}

@Composable
actual fun getScreenWidth() = LocalConfiguration.current.screenWidthDp.dp

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

actual fun shareText(text: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val intent = Intent.createChooser(sendIntent, null).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    AndroidContextProvider.applicationContext.startActivity(intent)
}

actual fun openWebsite(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    AndroidContextProvider.applicationContext.startActivity(browserIntent)
}