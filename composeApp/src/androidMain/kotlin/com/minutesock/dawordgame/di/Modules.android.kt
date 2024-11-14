package com.minutesock.dawordgame.di

import androidx.activity.ComponentActivity
import com.minutesock.dawordgame.AndroidSystemUiController
import com.minutesock.dawordgame.SystemUiController
import com.minutesock.dawordgame.core.data.AndroidDataStoreProvider
import com.minutesock.dawordgame.core.data.DataStoreProvider
import com.minutesock.dawordgame.core.data.DatabaseDriverFactory
import com.minutesock.dawordgame.core.data.ProductionDatabaseDriverFactory
import org.koin.dsl.bind
import org.koin.dsl.module

val androidModule = module {
    single { AndroidDataStoreProvider(get()) }.bind<DataStoreProvider>()
    single { ProductionDatabaseDriverFactory(get()) }.bind<DatabaseDriverFactory>()
}

fun androidUiModule(activity: ComponentActivity) = module {
    single { AndroidSystemUiController(activity) }.bind<SystemUiController>()
}