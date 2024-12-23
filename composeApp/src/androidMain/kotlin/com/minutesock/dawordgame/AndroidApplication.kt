package com.minutesock.dawordgame

import android.app.Application
import com.minutesock.dawordgame.di.androidModule
import com.minutesock.dawordgame.di.initKoin
import org.koin.android.ext.koin.androidContext

class AndroidApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidContextProvider.init(this)
        // todo add a ui module for SystemUiController
        initKoin {
            androidContext(this@AndroidApplication)
            modules(androidModule)
        }
    }
}