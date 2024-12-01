package com.minutesock.dawordgame.core.di

import com.minutesock.dawordgame.IosSystemUiController
import com.minutesock.dawordgame.SystemUiController
import com.minutesock.dawordgame.core.data.DataStoreProvider
import com.minutesock.dawordgame.core.data.DatabaseDriverFactory
import com.minutesock.dawordgame.core.data.NativeDataStoreProvider
import com.minutesock.dawordgame.core.data.ProductionDatabaseDriverFactory
import org.koin.dsl.bind
import org.koin.dsl.module

val iosModule = module {
    single { NativeDataStoreProvider() }.bind<DataStoreProvider>()
    single { ProductionDatabaseDriverFactory() }.bind<DatabaseDriverFactory>()
}

val iosUiModule = module {
    single { IosSystemUiController() }.bind<SystemUiController>()
}