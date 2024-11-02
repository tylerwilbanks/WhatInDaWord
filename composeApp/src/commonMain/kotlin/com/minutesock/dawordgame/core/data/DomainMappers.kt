package com.minutesock.dawordgame.core.data

import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.GuessWord
import com.minutesock.dawordgame.core.domain.ValidWord
import com.minutesock.dawordgame.core.domain.WordSelection
import com.minutesock.dawordgame.core.domain.WordSession
import com.minutesock.dawordgame.core.domain.WordSessionState
import com.minutesock.dawordgame.sqldelight.ValidWordEntity
import com.minutesock.dawordgame.sqldelight.WordSelectionEntity
import com.minutesock.dawordgame.sqldelight.WordSessionEntity
import kotlinx.collections.immutable.ImmutableList
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

fun WordSessionEntity.toWordSession(guesses: ImmutableList<GuessWord>): WordSession =
    WordSession(
        date = date?.let { LocalDate.parse(date) }
            ?: Clock.System.todayIn(TimeZone.currentSystemDefault()),
        mysteryWord = mystery_word,
        language = GameLanguage.fromDb(language),
        maxAttempts = max_attempts.toInt(),
        gameMode = GameMode.fromDb(game_mode),
        state = WordSessionState.entries[state.toInt()],
        id = id,
        startTime = start_time?.let { Instant.parse(start_time) },
        guesses = guesses
    )