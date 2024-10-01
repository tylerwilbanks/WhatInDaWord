package com.minutesock.dawordgame.di

import com.minutesock.dawordgame.database.AppDatabaseConstructor
import org.koin.dsl.module

val sharedModule = module {
    single { AppDatabaseConstructor.initialize() }
}