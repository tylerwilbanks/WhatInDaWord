package com.minutesock.dawordgame.di

import com.minutesock.dawordgame.database.AppDatabaseConstructor
import com.minutesock.dawordgame.feature.game.GameViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single { AppDatabaseConstructor.initialize() }
    viewModelOf(::GameViewModel)
}