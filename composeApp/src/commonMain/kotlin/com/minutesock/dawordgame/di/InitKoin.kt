package com.minutesock.dawordgame.di

import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin {
        config?.invoke(this)
        modules(appModule)
        KoinProvider.set(this.koin)
    }
}

object KoinProvider {
    private lateinit var _instance: Koin
    val instance get() = _instance

    fun set(koin: Koin) {
        _instance = koin
    }
}