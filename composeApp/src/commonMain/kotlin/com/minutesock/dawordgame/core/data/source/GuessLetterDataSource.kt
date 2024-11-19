package com.minutesock.dawordgame.core.data.source

import com.minutesock.dawordgame.core.data.SqlDelightDbClient
import com.minutesock.dawordgame.core.data.toGuessLetter
import com.minutesock.dawordgame.core.domain.GuessLetter
import com.minutesock.dawordgame.sqldelight.GuessLetterEntity

interface GuessLetterDataSource {
    suspend fun upsert(vararg guessLetters: GuessLetter)
    suspend fun selectByGuessWordId(guessWordId: Long): List<GuessLetter>
    suspend fun getCount(): Long
    suspend fun clearTable()
}

class SqlDelightGuessLetterDataSource(
    private val dbClient: SqlDelightDbClient
) : GuessLetterDataSource {
    private val queries = dbClient.guessLetterEntityQueries

    override suspend fun upsert(vararg guessLetters: GuessLetter) {
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

    override suspend fun selectByGuessWordId(guessWordId: Long): List<GuessLetter> {
        return dbClient.suspendingTransaction {
            queries.selectGuessLetterEntitiesByGuessWordId(guessWordId)
                .executeAsList()
                .map(GuessLetterEntity::toGuessLetter)
        }
    }

    override suspend fun getCount(): Long {
        return dbClient.suspendingTransaction {
            queries.selectCount().executeAsOne()
        }
    }

    override suspend fun clearTable() {
        dbClient.suspendingTransaction {
            queries.clearGuessLetterEntities()
        }
    }
}