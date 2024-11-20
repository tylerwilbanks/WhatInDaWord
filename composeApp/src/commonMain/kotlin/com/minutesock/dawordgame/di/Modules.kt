package com.minutesock.dawordgame.di

import com.minutesock.dawordgame.StatefulSystemUiController
import com.minutesock.dawordgame.core.data.DatabaseDriverFactory
import com.minutesock.dawordgame.core.data.DbClient
import com.minutesock.dawordgame.core.data.SqlDelightDbClient
import com.minutesock.dawordgame.core.data.SqlDelightValidWordDataSource
import com.minutesock.dawordgame.core.data.SqlDelightWordSelectionDataSource
import com.minutesock.dawordgame.core.data.TestDatabaseDriverFactory
import com.minutesock.dawordgame.core.data.ValidWordDataSource
import com.minutesock.dawordgame.core.data.WordSelectionDataSource
import com.minutesock.dawordgame.core.data.source.GuessLetterDataSource
import com.minutesock.dawordgame.core.data.source.GuessWordDataSource
import com.minutesock.dawordgame.core.data.source.SqlDelightGuessLetterDataSource
import com.minutesock.dawordgame.core.data.source.SqlDelightGuessWordDataSource
import com.minutesock.dawordgame.core.data.source.SqlDelightWordEntryDataSource
import com.minutesock.dawordgame.core.data.source.SqlDelightWordSessionDataSource
import com.minutesock.dawordgame.core.data.source.WordEntryDataSource
import com.minutesock.dawordgame.core.data.source.WordSessionDataSource
import com.minutesock.dawordgame.core.remote.createHttpClient
import com.minutesock.dawordgame.core.remote.createHttpClientEngine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { get<DatabaseDriverFactory>().createDriver() }
    single { SqlDelightDbClient(get(), Dispatchers.IO) }.bind<DbClient>()
    single { StatefulSystemUiController(get()) }
    singleOf(::SqlDelightValidWordDataSource).bind<ValidWordDataSource>()
    singleOf(::SqlDelightWordSelectionDataSource).bind<WordSelectionDataSource>()
    singleOf(::SqlDelightGuessLetterDataSource).bind<GuessLetterDataSource>()
    singleOf(::SqlDelightGuessWordDataSource).bind<GuessWordDataSource>()
    singleOf(::SqlDelightWordSessionDataSource).bind<WordSessionDataSource>()
    singleOf(::SqlDelightWordEntryDataSource).bind<WordEntryDataSource>()
    single { createHttpClient(createHttpClientEngine()) }
}

fun testDbModule(coroutineDispatcher: CoroutineDispatcher): Module {
    return module {
        single { TestDatabaseDriverFactory() }.bind<DatabaseDriverFactory>()
        single { get<DatabaseDriverFactory>().createDriver() }
        single { SqlDelightDbClient(get(), coroutineDispatcher) }.bind<DbClient>()
        singleOf(::SqlDelightValidWordDataSource).bind<ValidWordDataSource>()
        singleOf(::SqlDelightWordSelectionDataSource).bind<WordSelectionDataSource>()
        singleOf(::SqlDelightGuessLetterDataSource).bind<GuessLetterDataSource>()
        singleOf(::SqlDelightGuessWordDataSource).bind<GuessWordDataSource>()
        singleOf(::SqlDelightWordSessionDataSource).bind<WordSessionDataSource>()
        singleOf(::SqlDelightWordEntryDataSource).bind<WordEntryDataSource>()
        single { createHttpClient(createHttpClientEngine()) }
    }
}