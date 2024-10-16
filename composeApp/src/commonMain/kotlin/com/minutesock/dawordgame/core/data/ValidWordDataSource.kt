package com.minutesock.dawordgame.core.data

import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.ValidWord
import com.minutesock.dawordgame.sqldelight.ValidWordEntity

interface ValidWordDataSource {
    suspend fun clearValidWordEntities()
    suspend fun upsertValidWords(validWords: List<ValidWord>)
    suspend fun selectAllValidWords(language: GameLanguage): List<ValidWord>
    suspend fun selectValidWord(word: String, language: GameLanguage): ValidWord
}

class SqlDelightValidWordDataSource(
    private val dbClient: SqlDelightDbClient
) : ValidWordDataSource {
    private val validWordQueries = dbClient.validWordEntityQueries

    override suspend fun clearValidWordEntities() {
        dbClient.suspendingTransaction {
            validWordQueries.clearValidWordEntities()
        }
    }

    override suspend fun upsertValidWords(validWords: List<ValidWord>) {
        dbClient.suspendingTransaction {
            validWords.forEach {
                validWordQueries.upsertValidWordEntity(it.word, it.language.dbName)
            }
        }
    }

    override suspend fun selectAllValidWords(language: GameLanguage): List<ValidWord> {
        return dbClient.suspendingTransaction {
            validWordQueries
                .selectValidWordEntities(language = language.dbName)
                .executeAsList()
                .map { it.toValidWord() }
        }
    }

    override suspend fun selectValidWord(word: String, language: GameLanguage): ValidWord {
        return dbClient.suspendingTransaction {
            validWordQueries
                .selectValidWordEntity(word = word, language = language.dbName)
                .executeAsOne()
                .toValidWord()
        }
    }

}