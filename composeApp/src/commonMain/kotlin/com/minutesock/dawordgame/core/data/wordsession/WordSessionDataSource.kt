package com.minutesock.dawordgame.core.data.wordsession

import com.minutesock.dawordgame.core.data.SqlDelightDbClient
import com.minutesock.dawordgame.core.data.guessletter.GuessLetter
import com.minutesock.dawordgame.core.data.guessword.GuessWord
import com.minutesock.dawordgame.core.data.guessword.GuessWordState
import com.minutesock.dawordgame.core.data.letFromDb
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.GuessLetterState
import com.minutesock.dawordgame.sqldelight.GuessLetterEntity
import com.minutesock.dawordgame.sqldelight.GuessWordEntity
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
    private val wordSessionQueries = dbClient.wordSessionEntityQueries
    private val guessWordQueries = dbClient.guessWordEntityQueries
    private val guessLetterQueries = dbClient.guessLetterEntityQueries

    override suspend fun upsertWordSession(wordSession: WordSession) {
        dbClient.suspendingTransaction {
            wordSessionQueries.upsertWordSessionEntity(
                id = wordSession.idForDbInsertion,
                start_time = wordSession.startTime.toString(),
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

    override suspend fun selectWordSessionEntitiesByDate(
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
                .map { wordSessionEntity: SelectWordSessionEntitiesByDate ->
                    WordSession(
                        id = wordSessionEntity.id,
                        date = LocalDate.parse(wordSessionEntity.date.toString()),
                        startTime = wordSessionEntity.start_time.letFromDb {
                            Instant.parse(wordSessionEntity.start_time.toString())
                        },
                        mysteryWord = wordSessionEntity.mystery_word,
                        language = GameLanguage.fromDb(wordSessionEntity.language),
                        maxAttempts = wordSessionEntity.max_attempts.toInt(),
                        gameMode = GameMode.fromDb(wordSessionEntity.game_mode),
                        state = WordSessionState.entries[wordSessionEntity.state.toInt()],
                        guesses = guessWordQueries.selectGuessWordEntitiesBySessionId(
                            wordSessionEntity.id
                        ).executeAsList().map { guessWordEntity: GuessWordEntity ->
                            GuessWord(
                                id = guessWordEntity.id,
                                state = GuessWordState.entries[guessWordEntity.state.toInt()],
                                completeTime = guessWordEntity.complete_time.letFromDb {
                                    Instant.parse(it)
                                },
                                letters = guessLetterQueries.selectGuessLetterEntitiesByGuessWordId(
                                    guessWordEntity.id
                                ).executeAsList().map { guessLetterEntity: GuessLetterEntity ->
                                    GuessLetter(
                                        id = guessLetterEntity.id,
                                        character = guessLetterEntity.character.first(),
                                        state = GuessLetterState.entries[guessLetterEntity.state.toInt()]
                                    )
                                }
                            )
                        }
                    )
                }
        }
    }

    override suspend fun selectWordSessionEntity(id: Long): WordSession? {
        return dbClient.suspendingTransaction {
            wordSessionQueries.selectWordSessionEntity(id)
                .executeAsList()
                .map { wordSessionEntity: SelectWordSessionEntity ->
                    WordSession(
                        id = wordSessionEntity.id,
                        date = wordSessionEntity.date?.let { LocalDate.parse(it) }
                            ?: Clock.System.todayIn(
                                TimeZone.currentSystemDefault()
                            ),
                        mysteryWord = wordSessionEntity.mystery_word,
                        language = GameLanguage.fromDb(wordSessionEntity.language),
                        maxAttempts = wordSessionEntity.max_attempts.toInt(),
                        gameMode = GameMode.fromDb(wordSessionEntity.game_mode),
                        state = WordSessionState.entries[wordSessionEntity.state.toInt()],
                        startTime = wordSessionEntity.start_time.letFromDb {
                            Instant.parse(it)
                        },
                        guesses = guessWordQueries.selectGuessWordEntitiesBySessionId(
                            wordSessionEntity.id
                        ).executeAsList().map { guessWordEntity: GuessWordEntity ->
                            GuessWord(
                                id = guessWordEntity.id,
                                state = GuessWordState.entries[guessWordEntity.state.toInt()],
                                completeTime = guessWordEntity.complete_time.letFromDb {
                                    Instant.parse(it)
                                },
                                letters = guessLetterQueries.selectGuessLetterEntitiesByGuessWordId(
                                    guessWordEntity.id
                                ).executeAsList().map { guessLetterEntity: GuessLetterEntity ->
                                    GuessLetter(
                                        id = guessLetterEntity.id,
                                        character = guessLetterEntity.character.first(),
                                        state = GuessLetterState.entries[guessLetterEntity.state.toInt()]
                                    )
                                }
                            )
                        }
                    )
                }
                .firstOrNull()
        }
    }

    override suspend fun selectHighestId(): Long {
        return dbClient.suspendingTransaction {
            wordSessionQueries.selectHighestId().executeAsOneOrNull()?.max_id ?: 0L
        }
    }

    override suspend fun clearTable() {
        dbClient.suspendingTransaction {
            wordSessionQueries.clearWordSessionEntities()
        }
    }
}