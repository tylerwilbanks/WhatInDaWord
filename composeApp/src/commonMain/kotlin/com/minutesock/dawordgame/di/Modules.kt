package com.minutesock.dawordgame.di

import com.minutesock.dawordgame.database.AppDatabaseConstructor
import com.minutesock.dawordgame.feature.game.GameSetupHelper
import com.minutesock.dawordgame.feature.game.GameViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { AppDatabaseConstructor.initialize() }
    viewModelOf(::GameViewModel)

    single { GameSetupHelper(get()) }
}

val testModule = module {
    single { AppDatabaseConstructor.initialize() }
    single { GameSetupHelper(get()) }
}