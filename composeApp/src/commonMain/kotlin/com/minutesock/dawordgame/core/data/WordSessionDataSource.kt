package com.minutesock.dawordgame.core.data

import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.GuessLetter
import com.minutesock.dawordgame.core.domain.GuessLetterState
import com.minutesock.dawordgame.core.domain.GuessWord
import com.minutesock.dawordgame.core.domain.GuessWordState
import com.minutesock.dawordgame.core.domain.WordSession
import com.minutesock.dawordgame.core.domain.WordSessionState
import com.minutesock.dawordgame.sqldelight.SelectWordSessionEntitiesByDate
import com.minutesock.dawordgame.sqldelight.SelectWordSessionEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

interface WordSessionDataSource {
    suspend fun upsertWordSession(wordSession: WordSession)
    suspend fun selectWordSessionEntitiesByDate(
        date: LocalDate,
        language: GameLanguage,
        gameMode: GameMode
    ): List<WordSession>

    suspend fun selectWordSessionEntity(id: Long): WordSession?
    suspend fun selectHighestId(): Long
    suspend fun clearTable()
}

class SqlDelightWordSessionDataSource(
    private val dbClient: SqlDelightDbClient
) : WordSessionDataSource {
    private val queries = dbClient.wordSessionEntityQueries

    override suspend fun upsertWordSession(wordSession: WordSession) {
        dbClient.suspendingTransaction {
            queries.upsertWordSessionEntity(
                id = wordSession.id,
                start_time = wordSession.startTime.toString(),
                date = wordSession.date.toString(),
                mystery_word = wordSession.mysteryWord,
                language = wordSession.language.dbName,
                max_attempts = wordSession.maxAttempts.toLong(),
                game_mode = wordSession.gameMode.dbName,
                state = wordSession.state.ordinal.toLong()
            )
        }
    }

    override suspend fun selectWordSessionEntitiesByDate(
        date: LocalDate,
        language: GameLanguage,
        gameMode: GameMode
    ): List<WordSession> {
        return dbClient.suspendingTransaction {
            queries.selectWordSessionEntitiesByDate(
                date = date.toString(),
                language = language.dbName,
                game_mode = gameMode.dbName
            ).executeAsList()
                .groupBy { row: SelectWordSessionEntitiesByDate ->
                    WordSession(
                        id = row.id,
                        date = LocalDate.parse(row.date.toString()),
                        startTime = row.start_time.letFromDb {
                            Instant.parse(row.start_time.toString())
                        },
                        mysteryWord = row.mystery_word,
                        language = GameLanguage.fromDb(row.language),
                        maxAttempts = row.max_attempts.toInt(),
                        gameMode = GameMode.fromDb(row.game_mode),
                        state = WordSessionState.entries[row.state.toInt()],
                    )
                }
                .map { (wordSession: WordSession, rows: List<SelectWordSessionEntitiesByDate>) ->
                    val guessWords = rows
                        .filter { it.guess_word_id != null }
                        .groupBy { row ->
                            GuessWord(
                                id = row.guess_word_id!!,
                                state = GuessWordState.entries[row.guess_word_state!!.toInt()],
                                completeTime =
                                row.guess_word_complete_time.letFromDb {
                                    Instant.parse(it)
                                },
                                letters = emptyList()
                            )
                        }
                        .map { (guessWord: GuessWord, guessWordRows: List<SelectWordSessionEntitiesByDate>) ->
                            guessWord.copy(
                                letters = guessWordRows
                                    .filter { it.guess_letter_id != null }
                                    .map { row ->
                                        GuessLetter(
                                            id = row.guess_letter_id!!,
                                            character = row.guess_letter_character!!.first(),
                                            state = GuessLetterState.entries[row.state.toInt()]
                                        )
                                    }
                            )
                        }
                    wordSession.copy(guesses = guessWords)
                }
        }
    }

    override suspend fun selectWordSessionEntity(id: Long): WordSession? {
        return dbClient.suspendingTransaction {
            queries.selectWordSessionEntity(id)
                .executeAsList()
                .groupBy { row: SelectWordSessionEntity ->
                    WordSession(
                        id = row.id,
                        date = row.date?.let { LocalDate.parse(it) } ?: Clock.System.todayIn(
                            TimeZone.currentSystemDefault()
                        ),
                        mysteryWord = row.mystery_word,
                        language = GameLanguage.fromDb(row.language),
                        maxAttempts = row.max_attempts.toInt(),
                        gameMode = GameMode.fromDb(row.game_mode),
                        state = WordSessionState.entries[row.state.toInt()],
                        startTime = row.start_time.letFromDb {
                            Instant.parse(it)
                        }
                    )
                }
                .map { (wordSession: WordSession, rows: List<SelectWordSessionEntity>) ->
                    val guessWords = rows
                        .filter { it.guess_word_id != null }
                        .groupBy { row ->
                            GuessWord(
                                id = row.guess_word_id!!,
                                state = GuessWordState.entries[row.guess_word_state!!.toInt()],
                                completeTime =
                                row.guess_word_complete_time.letFromDb {
                                    Instant.parse(it)
                                },
                                letters = emptyList()
                            )
                        }
                        .map { (guessWord: GuessWord, guessWordRows: List<SelectWordSessionEntity>) ->
                            guessWord.copy(
                                letters = guessWordRows
                                    .filter { it.guess_letter_id != null }
                                    .map { row ->
                                        GuessLetter(
                                            id = row.guess_letter_id!!,
                                            character = row.guess_letter_character!!.first(),
                                            state = GuessLetterState.entries[row.state.toInt()]
                                        )
                                    }
                            )
                        }
                    wordSession.copy(guesses = guessWords)
                }.firstOrNull()
        }
    }

    override suspend fun selectHighestId(): Long {
        return dbClient.suspendingTransaction {
            queries.selectHighestId().executeAsOneOrNull()?.max_id ?: 0L
        }
    }

    override suspend fun clearTable() {
        dbClient.suspendingTransaction {
            queries.clearWordSessionEntities()
        }
    }
}