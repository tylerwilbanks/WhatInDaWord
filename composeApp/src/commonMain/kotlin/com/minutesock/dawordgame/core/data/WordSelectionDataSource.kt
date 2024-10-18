package com.minutesock.dawordgame.core.data

import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.WordSelection

interface WordSelectionDataSource {
    suspend fun upsertWordSelections(vararg wordSelections: WordSelection)
    suspend fun selectWordSelection(wordSelection: WordSelection): WordSelection
    suspend fun selectWordSelectionEntities(gameLanguage: GameLanguage): List<WordSelection>
    suspend fun getWordSelectionCount(): Long
    suspend fun clearWordSelectionTable()
}

class SqlDelightWordSelectionDataSource(
    private val dbClient: SqlDelightDbClient
) : WordSelectionDataSource {
    private val queries = dbClient.wordSelectionEntityQueries

    override suspend fun upsertWordSelections(vararg wordSelections: WordSelection) {
        dbClient.suspendingTransaction {
            wordSelections.forEach {
                queries.upsertWordSelectionEntity(it.word, it.language.dbName)
            }
        }
    }

    override suspend fun selectWordSelection(wordSelection: WordSelection): WordSelection {
        return dbClient.suspendingTransaction {
            queries
                .selectWordSelectionEntity(wordSelection.word, wordSelection.language.dbName)
                .executeAsOne()
                .toWordSelection()
        }
    }

    override suspend fun selectWordSelectionEntities(gameLanguage: GameLanguage): List<WordSelection> {
        return dbClient.suspendingTransaction {
            queries
                .selectWordSelectionEntities(language = gameLanguage.dbName)
                .executeAsList()
                .map { it.toWordSelection() }
        }
    }

    override suspend fun getWordSelectionCount() =
        dbClient.suspendingTransaction {
            queries.getWordSelectionEntityCount().executeAsOne()
        }

    override suspend fun clearWordSelectionTable() {
        dbClient.suspendingTransaction {
            queries.clearWordSelectionEntities()
        }
    }
}