package com.minutesock.dawordgame.core.data

import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.ValidWord

interface ValidWordDataSource {
    suspend fun clearTable()
    suspend fun upsert(validWords: List<ValidWord>)
    suspend fun selectAll(language: GameLanguage): List<ValidWord>
    suspend fun select(word: String, language: GameLanguage): ValidWord
    suspend fun getCount(): Long
}

class SqlDelightValidWordDataSource(
    private val dbClient: SqlDelightDbClient
) : ValidWordDataSource {
    private val validWordQueries = dbClient.validWordEntityQueries

    override suspend fun clearTable() {
        dbClient.suspendingTransaction {
            validWordQueries.clearValidWordEntities()
        }
    }

    override suspend fun upsert(validWords: List<ValidWord>) {
        dbClient.suspendingTransaction {
            validWords.forEach {
                validWordQueries.upsertValidWordEntity(it.word, it.language.dbName)
            }
        }
    }

    override suspend fun selectAll(language: GameLanguage): List<ValidWord> {
        return dbClient.suspendingTransaction {
            validWordQueries
                .selectValidWordEntities(language = language.dbName)
                .executeAsList()
                .map { it.toValidWord() }
        }
    }

    override suspend fun select(word: String, language: GameLanguage): ValidWord {
        return dbClient.suspendingTransaction {
            validWordQueries
                .selectValidWordEntity(word = word, language = language.dbName)
                .executeAsOne()
                .toValidWord()
        }
    }

    override suspend fun getCount() =
        dbClient.suspendingTransaction {
            validWordQueries.getValidWordEntityCount().executeAsOne()
        }
}