package com.minutesock.dawordgame

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun readFile(filename: String): String {
   val context = AndroidContextProvider.applicationContext
   return context.assets.open("src/commonMain/composeResources/$filename").bufferedReader().use { it.readText() }
}