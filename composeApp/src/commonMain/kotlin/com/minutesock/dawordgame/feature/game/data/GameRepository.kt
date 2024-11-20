package com.minutesock.dawordgame.feature.game.data

import com.minutesock.dawordgame.core.data.ValidWordDataSource
import com.minutesock.dawordgame.core.data.WordSelectionDataSource
import com.minutesock.dawordgame.core.data.source.GuessLetterDataSource
import com.minutesock.dawordgame.core.data.source.GuessWordDataSource
import com.minutesock.dawordgame.core.data.source.WordEntryDataSource
import com.minutesock.dawordgame.core.data.source.WordSessionDataSource
import com.minutesock.dawordgame.core.data.toWordEntry
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.GuessLetter
import com.minutesock.dawordgame.core.domain.GuessLetterState
import com.minutesock.dawordgame.core.domain.GuessWord
import com.minutesock.dawordgame.core.domain.GuessWordState
import com.minutesock.dawordgame.core.domain.WordSelection
import com.minutesock.dawordgame.core.domain.WordSession
import com.minutesock.dawordgame.core.domain.WordSessionState
import com.minutesock.dawordgame.core.domain.definition.WordEntry
import com.minutesock.dawordgame.core.uiutil.TextRes
import com.minutesock.dawordgame.core.util.ContinuousOption
import com.minutesock.dawordgame.core.util.ContinuousStatus
import com.minutesock.dawordgame.core.util.GeneralIssue
import com.minutesock.dawordgame.core.util.Option
import com.minutesock.dawordgame.di.KoinProvider
import com.minutesock.dawordgame.feature.game.GameSetupHelper
import com.minutesock.dawordgame.feature.game.remote.WordHttpClient
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.random.Random

class GameRepository(
    private val validWordDataSource: ValidWordDataSource = KoinProvider.instance.get(),
    private val wordSelectionDataSource: WordSelectionDataSource = KoinProvider.instance.get(),
    private val wordSessionDataSource: WordSessionDataSource = KoinProvider.instance.get(),
    private val guessWordDataSource: GuessWordDataSource = KoinProvider.instance.get(),
    private val guessLetterDataSource: GuessLetterDataSource = KoinProvider.instance.get(),
    private val wordEntryDataSource: WordEntryDataSource = KoinProvider.instance.get(),
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val wordHttpClient: WordHttpClient = WordHttpClient(defaultDispatcher = defaultDispatcher)
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
            val startIndex = gameLanguage.idStartOffset.toInt() - (gameLanguage.ordinal + 1)
            val chosenId = Random(date.dayOfYear).nextInt(
                from = startIndex,
                until = wordSelectionDataSource.getCount(gameLanguage).toInt()
            ).toLong()
            wordSelectionDataSource.selectById(chosenId)!!
        }
    }

    suspend fun selectMysteryWord(gameLanguage: GameLanguage): WordSelection {
        return withContext(defaultDispatcher) {
            val startIndex = gameLanguage.idStartOffset.toInt() - (gameLanguage.ordinal + 1)
            val chosenId = Random.nextInt(
                from = startIndex,
                until = wordSelectionDataSource.getCount(gameLanguage).toInt()
            ).toLong()
            wordSelectionDataSource.selectById(chosenId)!!
        }
    }

    suspend fun upsertWordSession(wordSession: WordSession) {
        withContext(defaultDispatcher) {
            wordSessionDataSource.upsert(wordSession)
        }
    }

    suspend fun getOrCreateWordSessionInfinityMode(
        language: GameLanguage,
        mysteryWord: String,
        date: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
        maxAttempts: Int = 6,
        wordLength: Int = 5
    ): WordSession {
        return withContext(defaultDispatcher) {
            wordSessionDataSource.selectByGameModeAndState(
                gameMode = GameMode.Infinity,
                state = WordSessionState.InProgress
            ) ?: wordSessionDataSource.selectByGameModeAndState(
                gameMode = GameMode.Infinity,
                state = WordSessionState.NotStarted
            ) ?: createAndInsertWordSession(
                date = date,
                language = language,
                gameMode = GameMode.Infinity,
                mysteryWord = mysteryWord,
                maxAttempts = maxAttempts,
                wordLength = wordLength
            )
        }
    }

    suspend fun getOrCreateWordSessionByDate(
        language: GameLanguage,
        gameMode: GameMode,
        mysteryWord: String,
        date: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
        maxAttempts: Int = 6,
        wordLength: Int = 5
    ): WordSession {
        return withContext(defaultDispatcher) {
            wordSessionDataSource.selectByDate(
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
                        sessionId = newWordSessionId,
                        letters = List(wordLength) {
                            GuessLetter(
                                guessWordId = newGuessWordId,
                                character = GuessLetter.AVAILABLE_CHAR,
                                state = GuessLetterState.Unknown
                            )
                        }.toImmutableList(),
                        state = if (index == 0) GuessWordState.Editing else GuessWordState.Unused,
                        completeTime = null
                    )
                }.toImmutableList(),
                gameMode = gameMode,
                state = WordSessionState.NotStarted
            ).also {
                wordSessionDataSource.upsert(wordSession = it)
            }
            val ws = wordSessionDataSource.selectById(newWordSessionId)
            ws!!
        }
    }

    fun getOrFetchWordEntry(language: GameLanguage, word: String, throttle: DatePeriod? = DatePeriod(days = 14)) =
        flow<ContinuousOption<WordEntry?, GeneralIssue>> {
            emit(
                ContinuousOption.Loading(
                    data = null,
                    continuousStatus = ContinuousStatus.Indefinite(textRes = TextRes.Raw("Loading word entry from database..."))
                )
            ) // todo translate loading message
            val wordEntryFromDatabase = wordEntryDataSource.selectByWord(language, word)
            val todayDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val shouldFetch = when {
                wordEntryFromDatabase == null -> true
                throttle == null -> true
                wordEntryFromDatabase.fetchDate.plus(throttle) <= todayDate -> true
                else -> false
            }

            if (shouldFetch) {
                emit(
                    ContinuousOption.Loading(
                        data = wordEntryFromDatabase,
                        continuousStatus = ContinuousStatus.Indefinite(
                            textRes = TextRes.Raw("Fetching word entry...")
                        )
                    )
                )
                val response = wordHttpClient.fetchWordDefinition(
                    gameLanguage = language,
                    word = word
                )
                when (response) {
                    is Option.Issue -> emit(
                        ContinuousOption.Issue(response.issue)
                    )

                    is Option.Success -> {
                        response.data.map { it.toWordEntry(language) }.first().let {
                            wordEntryDataSource.upsertEntriesAndDefinitions(it)
                        }
                        emit(
                            ContinuousOption.Success(
                                data = wordEntryDataSource.selectByWord(language, word)
                            )
                        )
                    }
                }
            }
        }
}