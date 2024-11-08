package com.minutesock.dawordgame.di

import com.minutesock.dawordgame.core.data.DataStoreProvider
import com.minutesock.dawordgame.core.data.DatabaseDriverFactory
import com.minutesock.dawordgame.core.data.DesktopDataStoreProvider
import com.minutesock.dawordgame.core.data.ProductionDatabaseDriverFactory
import org.koin.dsl.bind
import org.koin.dsl.module

val desktopModule = module {
    single { DesktopDataStoreProvider() }.bind<DataStoreProvider>()
    single { ProductionDatabaseDriverFactory() }.bind<DatabaseDriverFactory>()
}