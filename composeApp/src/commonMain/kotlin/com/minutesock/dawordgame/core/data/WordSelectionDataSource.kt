package com.minutesock.dawordgame.core.data

import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.WordSelection

interface WordSelectionDataSource {
    suspend fun upsert(vararg wordSelections: WordSelection)
    suspend fun select(wordSelection: WordSelection): WordSelection
    suspend fun selectById(id: Long): WordSelection?
    suspend fun selectAll(gameLanguage: GameLanguage): List<WordSelection>
    suspend fun getCount(gameLanguage: GameLanguage): Long
    suspend fun clearTable()
}

class SqlDelightWordSelectionDataSource(
    private val dbClient: SqlDelightDbClient
) : WordSelectionDataSource {
    private val queries = dbClient.wordSelectionEntityQueries

    override suspend fun upsert(vararg wordSelections: WordSelection) {
        dbClient.suspendingTransaction {
            wordSelections.forEach {
                queries.upsertWordSelectionEntity(it.idForDbInsertion, it.word, it.language.dbName)
            }
        }
    }

    override suspend fun select(wordSelection: WordSelection): WordSelection {
        return dbClient.suspendingTransaction {
            queries
                .selectWordSelectionEntity(wordSelection.word, wordSelection.language.dbName)
                .executeAsOne()
                .toWordSelection()
        }
    }

    override suspend fun selectById(id: Long): WordSelection? {
        return dbClient.suspendingTransaction {
            queries
                .selectWordSelectionEntityById(id = id)
                .executeAsOneOrNull()
                ?.toWordSelection()
        }
    }

    override suspend fun selectAll(gameLanguage: GameLanguage): List<WordSelection> {
        return dbClient.suspendingTransaction {
            queries
                .selectWordSelectionEntities(language = gameLanguage.dbName)
                .executeAsList()
                .map { it.toWordSelection() }
        }
    }

    override suspend fun getCount(gameLanguage: GameLanguage) =
        dbClient.suspendingTransaction {
            queries.getWordSelectionEntityCount(language = gameLanguage.dbName).executeAsOne()
        }

    override suspend fun clearTable() {
        dbClient.suspendingTransaction {
            queries.clearWordSelectionEntities()
        }
    }
}