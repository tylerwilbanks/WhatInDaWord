package com.minutesock.dawordgame.core.data.source

import com.minutesock.dawordgame.core.data.SqlDelightDbClient
import com.minutesock.dawordgame.core.data.toGuessWord
import com.minutesock.dawordgame.core.domain.GuessWord
import com.minutesock.dawordgame.sqldelight.GuessWordEntityQueries

interface GuessWordDataSource {
    suspend fun upsert(vararg guessWords: GuessWord)
    suspend fun selectByWordSessionId(wordSessionId: Long): List<GuessWord>
    suspend fun selectHighestId(): Long
    suspend fun getCount(): Long
    suspend fun clearTable()
}

class SqlDelightGuessWordDataSource(
    private val dbClient: SqlDelightDbClient
) : GuessWordDataSource {
    private val queries: GuessWordEntityQueries = dbClient.guessWordEntityQueries

    override suspend fun upsert(vararg guessWords: GuessWord) {
        dbClient.suspendingTransaction {
            guessWords.forEach { guessWord ->
                queries.upsertGuessWordEntity(
                    id = guessWord.idForDbInsertion,
                    session_id = guessWord.sessionId,
                    state = guessWord.state.ordinal.toLong(),
                    complete_time = guessWord.completeTime.toString()
                )
            }
        }
    }

    override suspend fun selectByWordSessionId(wordSessionId: Long): List<GuessWord> {
        return dbClient.suspendingTransaction {
            queries.selectGuessWordEntitiesBySessionId(wordSessionId)
                .executeAsList()
                .map { it.toGuessWord() }
        }
    }

    override suspend fun selectHighestId(): Long {
        return dbClient.suspendingTransaction {
            queries.selectHighestId().executeAsOneOrNull()?.max_id ?: 0L
        }
    }

    override suspend fun getCount(): Long {
        return dbClient.suspendingTransaction {
            queries.selectCount().executeAsOne()
        }
    }

    override suspend fun clearTable() {
        dbClient.suspendingTransaction {
            queries.clearGuessWordEntities()
        }
    }
}