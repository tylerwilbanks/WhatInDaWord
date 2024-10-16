package com.minutesock.dawordgame.di

import com.minutesock.dawordgame.core.data.DatabaseDriverFactory
import org.koin.dsl.module

val desktopModule = module {
    single { DatabaseDriverFactory() }
}