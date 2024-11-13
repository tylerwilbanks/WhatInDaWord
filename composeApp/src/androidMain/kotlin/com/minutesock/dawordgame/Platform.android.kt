package com.minutesock.dawordgame

import android.os.Build
import androidx.compose.runtime.Composable
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

//@Composable
//fun SystemUiController() {
//    val systemUiController = rememberSystemUi
//}