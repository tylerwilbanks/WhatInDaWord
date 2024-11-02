package com.minutesock.dawordgame.core.data.guessletter

import com.minutesock.dawordgame.core.data.SqlDelightDbClient
import com.minutesock.dawordgame.core.domain.GuessLetter

interface GuessLetterDataSource {
    suspend fun upsertGuessLetter(vararg guessLetters: GuessLetter)
    suspend fun clearTable()
}

class SqlDelightGuessLetterDataSource(
    private val dbClient: SqlDelightDbClient
) : GuessLetterDataSource {
    private val queries = dbClient.guessLetterEntityQueries

    override suspend fun upsertGuessLetter(vararg guessLetters: GuessLetter) {
        dbClient.suspendingTransaction {
            guessLetters.forEach { guessLetter ->
                queries.upsertGuessLetterEntity(
                    id = guessLetter.idForDbInsertion,
                    guess_word_id = guessLetter.guessWordId,
                    character = guessLetter.character.toString(),
                    state = guessLetter.state.ordinal.toLong()
                )
            }
        }
    }

    override suspend fun clearTable() {
        dbClient.suspendingTransaction {
            queries.clearGuessLetterEntities()
        }
    }
}