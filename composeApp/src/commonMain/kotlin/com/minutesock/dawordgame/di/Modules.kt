package com.minutesock.dawordgame.di

import com.minutesock.dawordgame.core.data.DatabaseDriverFactory
import com.minutesock.dawordgame.core.data.DbClient
import com.minutesock.dawordgame.core.data.SqlDelightDbClient
import com.minutesock.dawordgame.core.data.SqlDelightValidWordDataSource
import com.minutesock.dawordgame.core.data.SqlDelightWordSelectionDataSource
import com.minutesock.dawordgame.core.data.TestDatabaseDriverFactory
import com.minutesock.dawordgame.core.data.ValidWordDataSource
import com.minutesock.dawordgame.core.data.WordSelectionDataSource
import com.minutesock.dawordgame.core.data.guessletter.GuessLetterDataSource
import com.minutesock.dawordgame.core.data.guessletter.SqlDelightGuessLetterDataSource
import com.minutesock.dawordgame.core.data.guessword.GuessWordDataSource
import com.minutesock.dawordgame.core.data.guessword.SqlDelightGuessWordDataSource
import com.minutesock.dawordgame.core.data.wordsession.SqlDelightWordSessionDataSource
import com.minutesock.dawordgame.core.data.wordsession.WordSessionDataSource
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
    singleOf(::SqlDelightValidWordDataSource).bind<ValidWordDataSource>()
    singleOf(::SqlDelightWordSelectionDataSource).bind<WordSelectionDataSource>()
    singleOf(::SqlDelightGuessLetterDataSource).bind<GuessLetterDataSource>()
    singleOf(::SqlDelightGuessWordDataSource).bind<GuessWordDataSource>()
    singleOf(::SqlDelightWordSessionDataSource).bind<WordSessionDataSource>()
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
    }
}