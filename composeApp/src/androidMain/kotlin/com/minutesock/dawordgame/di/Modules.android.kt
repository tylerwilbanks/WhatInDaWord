package com.minutesock.dawordgame.di

import com.minutesock.dawordgame.core.data.DatabaseDriverFactory
import com.minutesock.dawordgame.core.data.ProductionDatabaseDriverFactory
import org.koin.dsl.bind
import org.koin.dsl.module

val androidModule = module {
    single { ProductionDatabaseDriverFactory(get()) }.bind<DatabaseDriverFactory>()
}