package com.minutesock.dawordgame.core.data

import com.minutesock.dawordgame.sqldelight.AppDatabase
import com.minutesock.dawordgame.sqldelight.GuessLetterEntityQueries
import com.minutesock.dawordgame.sqldelight.ValidWordEntityQueries
import com.minutesock.dawordgame.sqldelight.WordSelectionEntityQueries
import com.minutesock.dawordgame.sqldelight.WordSessionEntityQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

interface DbClient {
    fun <T> transaction(block: () -> T): T
    suspend fun <T> suspendingTransaction(block: suspend () -> T): T
}

class SqlDelightDbClient(
    driver: DatabaseDriverFactory,
    private val defaultDispatcher: CoroutineDispatcher,
) : DbClient {
    private val database = AppDatabase(driver.createDriver())
    val validWordEntityQueries: ValidWordEntityQueries
        get() = database.validWordEntityQueries

    val wordSelectionEntityQueries: WordSelectionEntityQueries
        get() = database.wordSelectionEntityQueries

    val wordSessionEntityQueries: WordSessionEntityQueries
        get() = database.wordSessionEntityQueries

    val guessLetterEntityQueries: GuessLetterEntityQueries
        get() = database.guessLetterEntityQueries

    override fun <T> transaction(block:() -> T): T {
        return database.transactionWithResult { block() }
    }

    override suspend fun <T> suspendingTransaction(block: suspend () -> T): T {
        return withContext(defaultDispatcher) {
            database.transactionWithResult {
                runBlocking { block() }
            }
        }
    }
}


