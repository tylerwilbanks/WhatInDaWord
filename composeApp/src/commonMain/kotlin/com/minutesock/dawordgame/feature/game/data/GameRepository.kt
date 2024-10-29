package com.minutesock.dawordgame.feature.game.data

import com.minutesock.dawordgame.core.data.GuessLetterDataSource
import com.minutesock.dawordgame.core.data.GuessWordDataSource
import com.minutesock.dawordgame.core.data.ValidWordDataSource
import com.minutesock.dawordgame.core.data.WordSelectionDataSource
import com.minutesock.dawordgame.core.data.WordSessionDataSource
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.GuessLetter
import com.minutesock.dawordgame.core.domain.GuessLetterState
import com.minutesock.dawordgame.core.domain.GuessWord
import com.minutesock.dawordgame.core.domain.GuessWordState
import com.minutesock.dawordgame.core.domain.WordSelection
import com.minutesock.dawordgame.core.domain.WordSession
import com.minutesock.dawordgame.core.domain.WordSessionState
import com.minutesock.dawordgame.feature.game.GameSetupHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.todayIn
import kotlin.random.Random

class GameRepository(
    private val validWordDataSource: ValidWordDataSource,
    private val wordSelectionDataSource: WordSelectionDataSource,
    private val wordSessionDataSource: WordSessionDataSource,
    private val guessWordDataSource: GuessWordDataSource,
    private val guessLetterDataSource: GuessLetterDataSource,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun setupWords(gameLanguage: GameLanguage) {
        withContext(defaultDispatcher) {
            GameSetupHelper(
                validWordDataSource = validWordDataSource,
                wordSelectionDataSource = wordSelectionDataSource,
                defaultDispatcher = defaultDispatcher
            ).apply {
                upsertValidWordsIfNeeded(gameLanguage)
                upsertWordSelectionIfNeeded(gameLanguage)
            }
        }
    }

    suspend fun selectMysteryWordByDate(
        gameLanguage: GameLanguage,
        date: LocalDateTime = Clock
            .System
            .todayIn(TimeZone.currentSystemDefault())
            .atTime(0, 0)
    ): WordSelection {
        return withContext(defaultDispatcher) {
            val wordSelection = wordSelectionDataSource.selectAll(gameLanguage)
            wordSelection[Random(date.dayOfYear).nextInt(from = 0, until = wordSelection.size)]
        }
    }

    suspend fun selectMysteryWord(gameLanguage: GameLanguage): WordSelection {
        return withContext(defaultDispatcher) {
            wordSelectionDataSource.selectAll(gameLanguage).random()
        }
    }

    suspend fun getOrCreateWordSessionByDate(
        date: LocalDate,
        language: GameLanguage,
        gameMode: GameMode,
        mysteryWord: String,
        maxAttempts: Int,
        wordLength: Int
    ): WordSession {
        return withContext(defaultDispatcher) {
            wordSessionDataSource.selectWordSessionEntitiesByDate(
                date = date,
                language = language,
                gameMode = gameMode
            ).firstOrNull() ?: createAndInsertWordSession(
                date = date,
                language = language,
                gameMode = gameMode,
                mysteryWord = mysteryWord,
                maxAttempts = maxAttempts,
                wordLength = wordLength
            )
        }
    }

    private suspend fun createAndInsertWordSession(
        date: LocalDate,
        language: GameLanguage,
        gameMode: GameMode,
        mysteryWord: String,
        maxAttempts: Int,
        wordLength: Int
    ): WordSession {
        return withContext(defaultDispatcher) {
            val newWordSessionId = wordSessionDataSource.selectHighestId() + 1
            val startingGuessWordId = guessWordDataSource.selectHighestId() + 1
            WordSession(
                id = newWordSessionId,
                date = date,
                mysteryWord = mysteryWord,
                language = language,
                maxAttempts = maxAttempts,
                guesses = List(maxAttempts) { index: Int ->
                    val newGuessWordId = startingGuessWordId + index
                    GuessWord(
                        id = newWordSessionId,
                        sessionId = newWordSessionId,
                        letters = List(wordLength) {
                            GuessLetter(
                                guessWordId = newGuessWordId,
                                character = GuessLetter.AVAILABLE_CHAR,
                                state = GuessLetterState.Unknown
                            )
                        },
                        state = if (index == 0) GuessWordState.Editing else GuessWordState.Unused,
                        completeTime = null
                    )
                },
                gameMode = gameMode,
                state = WordSessionState.NotStarted
            ).also {
                wordSessionDataSource.upsertWordSession(wordSession = it)
            }
        }
    }
}