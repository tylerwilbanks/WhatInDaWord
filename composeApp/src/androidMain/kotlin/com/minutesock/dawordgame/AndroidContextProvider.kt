package com.minutesock.dawordgame

import android.content.Context

object AndroidContextProvider {
    private lateinit var _applicationContext: Context
    val applicationContext get() = _applicationContext

    fun init(applicationContext: Context) {
        _applicationContext = applicationContext
    }
}