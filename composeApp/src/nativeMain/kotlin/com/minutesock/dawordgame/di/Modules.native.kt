package com.minutesock.dawordgame.di

import com.minutesock.dawordgame.core.data.DataStoreProvider
import com.minutesock.dawordgame.core.data.DatabaseDriverFactory
import com.minutesock.dawordgame.core.data.NativeDataStoreProvider
import com.minutesock.dawordgame.core.data.ProductionDatabaseDriverFactory
import org.koin.dsl.bind
import org.koin.dsl.module

val nativeModule = module {
    single { NativeDataStoreProvider() }.bind<DataStoreProvider>()
    single { ProductionDatabaseDriverFactory() }.bind<DatabaseDriverFactory>()
}