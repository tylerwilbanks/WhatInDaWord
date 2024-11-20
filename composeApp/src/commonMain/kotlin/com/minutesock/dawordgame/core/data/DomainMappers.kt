package com.minutesock.dawordgame.core.data

import com.minutesock.dawordgame.core.data.source.GuessLetterDataSource
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
import com.minutesock.dawordgame.core.domain.definition.WordDefinition
import com.minutesock.dawordgame.core.domain.definition.WordEntry
import com.minutesock.dawordgame.core.remote.definition.DefinitionDto
import com.minutesock.dawordgame.core.remote.definition.WordEntryDto
import com.minutesock.dawordgame.sqldelight.GuessLetterEntity
import com.minutesock.dawordgame.sqldelight.GuessWordEntity
import com.minutesock.dawordgame.sqldelight.ValidWordEntity
import com.minutesock.dawordgame.sqldelight.WordDefinitionEntity
import com.minutesock.dawordgame.sqldelight.WordEntryEntity
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

fun WordEntryEntity.toWordEntry(definitions: List<WordDefinition>): WordEntry {
    return WordEntry(
        id = id,
        language = GameLanguage.fromDb(language),
        word = word,
        fetchDate = fetch_date.letFromDb { LocalDate.parse(it) }
            ?: Clock.System.todayIn(TimeZone.currentSystemDefault()),
        definitions = definitions.toImmutableList(),
        phonetic = phonetic,
        origin = origin
    )
}

fun WordDefinitionEntity.toWordDefinition(): WordDefinition {
    return WordDefinition(
        id = 0,
        language = GameLanguage.fromDb(language),
        word = word,
        partOfSpeech = part_of_speech ?: "",
        definition = definition,
        example = example.letFromDb { example }
    )
}

fun DefinitionDto.toWordDefinition(word: String, language: GameLanguage, partOfSpeech: String): WordDefinition {
    return WordDefinition(
        id = 0,
        language = language,
        word = word,
        partOfSpeech = partOfSpeech,
        definition = definition,
        example = example
    )
}

fun WordEntryDto.toWordEntry(
    language: GameLanguage,
    fetchDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
): WordEntry {
    return WordEntry(
        language = language,
        word = word,
        fetchDate = fetchDate,
        definitions = meanings.map { meaning ->
            meaning.definitions.map {
                it.toWordDefinition(
                    word = word,
                    language = language,
                    partOfSpeech = meaning.partOfSpeech
                )
            }
        }
            .flatten()
            .toImmutableList()
    )
}