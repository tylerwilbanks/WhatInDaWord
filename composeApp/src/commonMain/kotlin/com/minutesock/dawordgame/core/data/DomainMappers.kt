package com.minutesock.dawordgame.core.data

import com.minutesock.dawordgame.core.data.guessletter.GuessLetterDataSource
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.GuessLetter
import com.minutesock.dawordgame.core.domain.GuessLetterState
import com.minutesock.dawordgame.core.domain.GuessWord
import com.minutesock.dawordgame.core.domain.GuessWordState
import com.minutesock.dawordgame.core.domain.ValidWord
import com.minutesock.dawordgame.core.domain.WordSelection
import com.minutesock.dawordgame.core.domain.WordSession
import com.minutesock.dawordgame.core.domain.WordSessionState
import com.minutesock.dawordgame.sqldelight.GuessLetterEntity
import com.minutesock.dawordgame.sqldelight.GuessWordEntity
import com.minutesock.dawordgame.sqldelight.ValidWordEntity
import com.minutesock.dawordgame.sqldelight.WordSelectionEntity
import com.minutesock.dawordgame.sqldelight.WordSessionEntity
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

fun ValidWordEntity.toValidWord(): ValidWord
    = ValidWord(id = id, word = word, language = GameLanguage.fromDb(language))

fun ValidWord.toValidWordEntity(): ValidWordEntity
    = ValidWordEntity(id = id, word = word, language = language.dbName)

fun WordSelectionEntity.toWordSelection(): WordSelection =
    WordSelection(id = id, word = word, language = GameLanguage.fromDb(language))

fun WordSelection.toWordSelectionEntity(): WordSelectionEntity =
    WordSelectionEntity(id = id, word = word, language = language.dbName)

fun WordSessionEntity.toWordSession(guesses: List<GuessWord>): WordSession =
    WordSession(
        id = id,
        date = date?.let { LocalDate.parse(date) }
            ?: Clock.System.todayIn(TimeZone.currentSystemDefault()),
        mysteryWord = mystery_word,
        language = GameLanguage.fromDb(language),
        maxAttempts = max_attempts.toInt(),
        gameMode = GameMode.fromDb(game_mode),
        state = WordSessionState.entries[state.toInt()],
        guesses = guesses.toImmutableList()
    )

fun GuessWordEntity.toGuessWord(): GuessWord {
    return GuessWord(
        id = id,
        sessionId = session_id,
        state = GuessWordState.entries[state.toInt()],
        completeTime = complete_time.letFromDb {
            Instant.parse(it)
        },
        letters = persistentListOf()
    )
}

fun GuessWordEntity.toGuessWord(letters: List<GuessLetter>): GuessWord {
    return GuessWord(
        id = id,
        sessionId = session_id,
        state = GuessWordState.entries[state.toInt()],
        completeTime = complete_time.letFromDb {
            Instant.parse(it)
        },
        letters = letters.toImmutableList()
    )
}

suspend fun GuessWordEntity.toGuessWord(guessLetterDataSource: GuessLetterDataSource): GuessWord {
    return GuessWord(
        id = id,
        sessionId = session_id,
        state = GuessWordState.entries[state.toInt()],
        completeTime = complete_time.letFromDb {
            Instant.parse(it)
        },
        letters = guessLetterDataSource.selectByGuessWordId(id).toImmutableList()
    )
}

fun GuessLetterEntity.toGuessLetter(): GuessLetter {
    return GuessLetter(
        id = id,
        guessWordId = guess_word_id,
        character = character.first(),
        state = GuessLetterState.entries[state.toInt()]
    )
}