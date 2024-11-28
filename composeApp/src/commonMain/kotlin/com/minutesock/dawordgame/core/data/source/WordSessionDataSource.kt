package com.minutesock.dawordgame.core.data.source

import com.minutesock.dawordgame.core.data.SqlDelightDbClient
import com.minutesock.dawordgame.core.data.toGuessLetter
import com.minutesock.dawordgame.core.data.toGuessWord
import com.minutesock.dawordgame.core.data.toWordSession
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.WordSession
import com.minutesock.dawordgame.core.domain.WordSessionState
import com.minutesock.dawordgame.sqldelight.GuessLetterEntity
import com.minutesock.dawordgame.sqldelight.GuessWordEntity
import com.minutesock.dawordgame.sqldelight.WordSessionEntity
import kotlinx.datetime.LocalDate

interface WordSessionDataSource {
    suspend fun upsert(wordSession: WordSession)
    suspend fun selectByDate(
        date: LocalDate,
        language: GameLanguage,
        gameMode: GameMode
    ): List<WordSession>

    suspend fun selectById(id: Long): WordSession?
    suspend fun selectByGameModeAndState(
        gameMode: GameMode,
        state: WordSessionState
    ): WordSession?
    suspend fun selectHighestId(): Long
    suspend fun deleteByDate(date: LocalDate, language: GameLanguage)
    suspend fun selectCompletedMysteryWordsSortedAlphabetically(language: GameLanguage): List<String>
    suspend fun selectCompleteWordSessionsCount(language: GameLanguage): Long
    suspend fun selectByMysteryWord(language: GameLanguage, mysteryWord: String): List<WordSession>
    suspend fun clearTable()
}

class SqlDelightWordSessionDataSource(
    private val dbClient: SqlDelightDbClient,
) : WordSessionDataSource {
    private val wordSessionQueries = dbClient.wordSessionEntityQueries
    private val guessWordQueries = dbClient.guessWordEntityQueries
    private val guessLetterQueries = dbClient.guessLetterEntityQueries

    override suspend fun upsert(wordSession: WordSession) {
        dbClient.suspendingTransaction {
            wordSessionQueries.upsertWordSessionEntity(
                id = wordSession.idForDbInsertion,
                date = wordSession.date.toString(),
                mystery_word = wordSession.mysteryWord,
                language = wordSession.language.dbName,
                max_attempts = wordSession.maxAttempts.toLong(),
                game_mode = wordSession.gameMode.dbName,
                state = wordSession.state.ordinal.toLong()
            )
            wordSession.guesses.forEach { guess ->
                guessWordQueries.upsertGuessWordEntity(
                    id = guess.idForDbInsertion,
                    session_id = guess.sessionId,
                    state = guess.state.ordinal.toLong(),
                    complete_time = guess.completeTime?.toString()
                )
                guess.letters.forEach { letter ->
                    guessLetterQueries.upsertGuessLetterEntity(
                        id = letter.idForDbInsertion,
                        character = letter.character.toString(),
                        guess_word_id = letter.guessWordId,
                        state = letter.state.ordinal.toLong()
                    )
                }
            }
        }
    }

    override suspend fun selectByDate(
        date: LocalDate,
        language: GameLanguage,
        gameMode: GameMode
    ): List<WordSession> {
        return dbClient.suspendingTransaction {
            wordSessionQueries.selectWordSessionEntitiesByDate(
                date = date.toString(),
                language = language.dbName,
                game_mode = gameMode.dbName
            ).executeAsList()
                .mapWordSessionEntities()
        }
    }

    override suspend fun selectById(id: Long): WordSession? {
        return dbClient.suspendingTransaction {
            wordSessionQueries.selectWordSessionEntity(id)
                .executeAsList()
                .mapWordSessionEntities()
                .firstOrNull()
        }
    }

    override suspend fun selectByGameModeAndState(
        gameMode: GameMode,
        state: WordSessionState
    ): WordSession? {
        return wordSessionQueries.selectWordSessionEntityByModeAndState(
            game_mode = gameMode.dbName,
            state = state.ordinal.toLong()
        ).executeAsList()
            .mapWordSessionEntities()
            .firstOrNull()
    }

    private fun List<WordSessionEntity>.mapWordSessionEntities(): List<WordSession> {
        return this.map { wordSessionEntity: WordSessionEntity ->
            wordSessionEntity.toWordSession(
                guesses = guessWordQueries.selectGuessWordEntitiesBySessionId(
                    wordSessionEntity.id
                ).executeAsList().map { guessWordEntity: GuessWordEntity ->
                    guessWordEntity.toGuessWord(
                        letters = guessLetterQueries.selectGuessLetterEntitiesByGuessWordId(
                            guessWordEntity.id
                        ).executeAsList().map { guessLetterEntity: GuessLetterEntity ->
                            guessLetterEntity.toGuessLetter()
                        }
                    )
                }
            )
        }
    }

    override suspend fun selectHighestId(): Long {
        return dbClient.suspendingTransaction {
            wordSessionQueries.selectHighestId().executeAsOneOrNull()?.max_id ?: 0L
        }
    }

    override suspend fun deleteByDate(date: LocalDate, language: GameLanguage) {
        dbClient.suspendingTransaction {
            wordSessionQueries.deleteByDate(date.toString(), language.dbName)
        }
    }

    override suspend fun selectCompletedMysteryWordsSortedAlphabetically(language: GameLanguage): List<String> {
        return dbClient.suspendingTransaction {
            wordSessionQueries.selectCompletedMysteryWordsSortedAlphabetically(
                language = language.dbName,
                state = listOf(WordSessionState.Success, WordSessionState.Failure).map { it.ordinal.toLong() }
            ).executeAsList()
        }
    }

    override suspend fun selectCompleteWordSessionsCount(language: GameLanguage): Long {
        return dbClient.suspendingTransaction {
            wordSessionQueries.selectCompletedSessionsCount(
                language = language.dbName,
                state = listOf(WordSessionState.Success, WordSessionState.Failure).map { it.ordinal.toLong() }
            ).executeAsOne()
        }
    }

    override suspend fun selectByMysteryWord(language: GameLanguage, mysteryWord: String): List<WordSession> {
        return dbClient.suspendingTransaction {
            wordSessionQueries.selectByMysteryWord(
                language = language.dbName,
                mystery_word = mysteryWord
            )
                .executeAsList()
                .mapWordSessionEntities()
        }
    }

    override suspend fun clearTable() {
        dbClient.suspendingTransaction {
            wordSessionQueries.clearWordSessionEntities()
        }
    }
}