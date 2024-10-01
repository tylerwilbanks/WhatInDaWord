package com.minutesock.dawordgame

import android.app.Application
import com.minutesock.dawordgame.di.initKoin
import org.koin.android.ext.koin.androidContext

class AndroidApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AndroidApplication)
        }
    }
}