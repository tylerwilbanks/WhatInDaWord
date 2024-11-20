package com.minutesock.dawordgame.core.data.source

import com.minutesock.dawordgame.core.data.SqlDelightDbClient
import com.minutesock.dawordgame.core.data.toWordDefinition
import com.minutesock.dawordgame.core.data.toWordEntry
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.definition.WordDefinition
import com.minutesock.dawordgame.core.domain.definition.WordEntry
import com.minutesock.dawordgame.sqldelight.WordDefinitionEntity
import com.minutesock.dawordgame.sqldelight.WordEntryEntity

interface WordEntryDataSource {
    suspend fun upsertEntriesAndDefinitions(wordEntry: WordEntry)
    suspend fun selectById(id: Long): WordEntry?
    suspend fun selectByWord(language: GameLanguage, word: String): WordEntry?
    suspend fun selectCountByWord(language: GameLanguage, word: String): Int
}

class SqlDelightWordEntryDataSource(
    private val dbClient: SqlDelightDbClient
) : WordEntryDataSource {
    private val wordEntryQueries = dbClient.wordEntryEntityQueries
    private val wordDefinitionQueries = dbClient.wordDefinitionEntityQueries

    override suspend fun upsertEntriesAndDefinitions(wordEntry: WordEntry) {
        dbClient.suspendingTransaction {
            wordEntryQueries.upsert(
                language = wordEntry.language.dbName,
                word = wordEntry.word,
                fetch_date = wordEntry.fetchDate.toString(),
                phonetic = wordEntry.phonetic,
                origin = wordEntry.origin
            )

            wordDefinitionQueries.deleteAll(wordEntry.word, wordEntry.language.dbName)

            wordEntry.definitions.forEach { def ->
                wordDefinitionQueries.insert(
                    language = def.language.dbName,
                    word = def.word,
                    part_of_speech = def.partOfSpeech,
                    definition = def.definition,
                    example = def.example
                )
            }
        }
    }

    private fun WordEntryEntity.selectDefinitions(language: GameLanguage): List<WordDefinition> {
        return wordDefinitionQueries.selectByWord(
            language = language.dbName,
            word = word
        )
            .executeAsList()
            .map(WordDefinitionEntity::toWordDefinition)
    }

    override suspend fun selectById(id: Long): WordEntry? {
        return dbClient.suspendingTransaction {
            wordEntryQueries.selectById(id)
                .executeAsOneOrNull()
                ?.let {
                    it.toWordEntry(
                        definitions = it.selectDefinitions(GameLanguage.fromDb(it.language))
                    )
                }
        }
    }

    override suspend fun selectByWord(language: GameLanguage, word: String): WordEntry? {
        return dbClient.suspendingTransaction {
            wordEntryQueries.selectByWord(word = word, language = language.dbName)
                .executeAsOneOrNull()
                ?.let {
                    it.toWordEntry(
                        definitions = it.selectDefinitions(language)
                    )
                }
        }
    }

    override suspend fun selectCountByWord(language: GameLanguage, word: String): Int {
        return dbClient.suspendingTransaction {
            wordEntryQueries.selectCountByWord(word, language.dbName).executeAsOne().toInt()
        }
    }
}