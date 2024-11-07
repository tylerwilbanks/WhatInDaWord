package com.minutesock.dawordgame.feature.game.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.GuessLetter
import com.minutesock.dawordgame.core.domain.GuessLetterState
import com.minutesock.dawordgame.core.domain.GuessWord
import com.minutesock.dawordgame.core.domain.GuessWordState
import com.minutesock.dawordgame.core.domain.WordSession
import com.minutesock.dawordgame.core.domain.WordSessionState
import com.minutesock.dawordgame.core.uiutil.TextRes
import com.minutesock.dawordgame.core.util.Option
import com.minutesock.dawordgame.feature.game.data.GameRepository
import com.minutesock.dawordgame.feature.game.domain.GuessWordValidator
import com.minutesock.dawordgame.feature.game.domain.WordGameValidationResultType
import com.minutesock.dawordgame.getSystemLanguage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import whatindaword.composeapp.generated.resources.Res
import whatindaword.composeapp.generated.resources.no_more_guess_attempts_left
import whatindaword.composeapp.generated.resources.what_in_da_word

class GameViewModel(
    private val gameRepository: GameRepository,
    private val guessWordValidator: GuessWordValidator,
) : ViewModel() {

    private var finalMessage: GameTitleMessage? = null

    private val _state = MutableStateFlow(GameViewModelState())
    val state = _state.asStateFlow()

    private val requireWordSession get() = state.value.wordSession!!


    fun setupGame(gameMode: GameMode, wordLength: Int = 5, attempts: Int = 6): Job {
        return viewModelScope.launch {
            _state.update {
                it.copy(
                    loadingState = GameLoadingState.Loading
                )
            }
            val gameLanguage = getSystemLanguage()
            gameRepository.setupWords(gameLanguage)
            val selectedWord = when (gameMode) {
                GameMode.Daily -> gameRepository.selectMysteryWordByDate(gameLanguage)
                GameMode.Infinity -> gameRepository.selectMysteryWord(gameLanguage)
            }

            val wordSession = when (gameMode) {
                GameMode.Daily -> gameRepository.getOrCreateWordSessionByDate(
                    language = gameLanguage,
                    gameMode = gameMode,
                    mysteryWord = selectedWord.word,
                    wordLength = wordLength,
                    maxAttempts = attempts
                )

                GameMode.Infinity -> gameRepository.getOrCreateWordSessionInfinityMode(
                    language = gameLanguage,
                    mysteryWord = selectedWord.word,
                    wordLength = wordLength,
                    maxAttempts = attempts
                )
            }

            _state.update {
                it.copyWithWordSession(
                    wordSession = wordSession,
                    loadingState = GameLoadingState.Loaded,
                    mysteryWord = selectedWord,
                    falseKeyboardKeys = getUpdatedFalseKeyboardKeys(
                        guessWords = wordSession.guesses,
                        falseKeyboardKeys = state.value.falseKeyboardKeys
                    )
                )
            }
        }
    }

    private fun getUpdatedFalseKeyboardKeys(
        guessWords: List<GuessWord>,
        falseKeyboardKeys: FalseKeyboardKeys
    ): FalseKeyboardKeys {
        val keys = hashMapOf<Char, GuessLetterState>()
        guessWords.forEach { guessWord ->
            guessWord.letters.forEach { guessLetter ->
                val maxOrdinal = keys.filter {
                    it.key == guessLetter.character && it.value.ordinal > guessLetter.state.ordinal
                }.values.maxOfOrNull { it.ordinal }
                val newState = if (maxOrdinal != null) {
                    GuessLetterState.entries[maxOrdinal]
                } else {
                    guessLetter.state
                }
                keys[guessLetter.character] = newState
            }
        }
        val keysWithNewState =
            keys.map { GuessKeyboardLetter(it.key.toString(), it.value) }.toImmutableList()
        val row1 = getUpdatedKeyboardRow(keysWithNewState, falseKeyboardKeys.row1)
        val row2 = getUpdatedKeyboardRow(keysWithNewState, falseKeyboardKeys.row2)
        val row3 = getUpdatedKeyboardRow(keysWithNewState, falseKeyboardKeys.row3)
        return FalseKeyboardKeys(row1, row2, row3)
    }

    private fun getUpdatedKeyboardRow(
        keysWithNewState: ImmutableList<GuessKeyboardLetter>,
        row: ImmutableList<GuessKeyboardLetter>
    ): ImmutableList<GuessKeyboardLetter> {
        val mutableRow = row.toMutableList()
        row.forEachIndexed { index, guessKey ->
            val a = keysWithNewState.firstOrNull { it.keyName == guessKey.keyName }
            mutableRow[index] = a ?: guessKey
        }
        return mutableRow.toImmutableList()
    }

    fun onEvent(event: WordGameEvent): Job {
        return viewModelScope.launch {
            when (event) {
                is WordGameEvent.OnAnsweredWordRowAnimationFinished -> event.onEvent()
                is WordGameEvent.OnCharacterPress -> event.onEvent()
                is WordGameEvent.OnCompleteAnimationFinished -> TODO()
                is WordGameEvent.OnDeletePress -> event.onEvent()
                is WordGameEvent.OnEnterPress -> event.onEvent()
                is WordGameEvent.OnErrorAnimationFinished -> event.onEvent()
                is WordGameEvent.OnStatsPress -> TODO()
            }
        }
    }

    private suspend fun WordGameEvent.OnAnsweredWordRowAnimationFinished.onEvent() {
        if (state.value.gameState.isGameOver &&
            !requireWordSession.guesses.any { it.state == GuessWordState.Correct || it.state == GuessWordState.Failure }
        ) {
            val index = requireWordSession.guesses.indexOfLast { it.state == GuessWordState.Complete }
            val updatedGuesses = getUpdatedWordRows(
                index = index,
                guessWord = requireWordSession.guesses[index].updateState(
                    if (state.value.gameState == WordSessionState.Success) {
                        GuessWordState.Correct
                    } else {
                        GuessWordState.Failure
                    }
                )
            )

            val newWordSession = requireWordSession.copy(guesses = updatedGuesses)
            gameRepository.upsertWordSession(newWordSession)

            _state.update {
                it.copy(
                    wordSession = newWordSession
                )
            }
        }

        _state.update {
            it.copy(
                wordRowAnimating = false,
                gameTitleMessage = finalMessage ?: GameTitleMessage(
                    message = guessWordValidator.obtainRandomMessageBasedOnGameState(requireWordSession.state),
                    isError = false
                )
            )
        }
    }

    private fun WordGameEvent.OnCharacterPress.onEvent() {
        getCurrentGuessWordIndexAndHandleError()?.let { index ->
            val result = requireWordSession.guesses[index].addGuessLetter(
                GuessLetter(
                    character = this.character,
                    state = GuessLetterState.Unknown
                )
            )

            when (result) {
                is Option.Error -> _state.update {
                    it.copy(
                        gameTitleMessage = GameTitleMessage(
                            message = result.textRes,
                            isError = true
                        )
                    )
                }

                is Option.Success -> {
                    _state.update { state ->
                        state.copy(
                            wordSession = requireWordSession.copy(
                                guesses = getUpdatedWordRows(index, result.data)
                            )
                        )
                    }
                }
            }
        }
    }

    private fun WordGameEvent.OnDeletePress.onEvent() {
        getCurrentGuessWordIndexAndHandleError()?.let { index: Int ->
            eraseLetter(index)
        }
    }

    private suspend fun WordGameEvent.OnEnterPress.onEvent() {
        if (state.value.gameState.isGameOver) {
            return
        }

        getCurrentGuessWordIndexAndHandleError()?.let { index: Int ->
            val currentGuessWord = requireWordSession.guesses[index]
            val result = guessWordValidator.validateGuess(
                guessWord = currentGuessWord,
                correctWord = state.value.mysteryWord.word,
                isFinalGuess = isFinalGuess(index),
                language = state.value.language
            )

            when (result.type) {
                WordGameValidationResultType.Unknown -> Unit
                WordGameValidationResultType.Error -> {
                    _state.update {
                        it.copy(
                            gameTitleMessage = GameTitleMessage(
                                message = result.textRes,
                                isError = true
                            )
                        )
                    }
                }

                WordGameValidationResultType.Incorrect -> {
                    val gameTitleMessage = GameTitleMessage(
                        message = result.textRes,
                        isError = isFinalGuess(index)
                    )
                    val incorrectGuessWord = requireWordSession.guesses[index]
                        .lockInGuess(state.value.mysteryWord.word, isFinalGuess(index))
                    if (isFinalGuess(index)) {
                        val newWordSession = runItThroughThePipes(
                            requireWordSession.copy(
                                guesses = getUpdatedWordRows(index, incorrectGuessWord),
                                state = WordSessionState.Failure
                            )
                        )
                        _state.update {
                            it.copy(
                                gameTitleMessage = gameTitleMessage,
                                wordRowAnimating = true,
                                wordSession = newWordSession,
                                falseKeyboardKeys = getUpdatedFalseKeyboardKeys(
                                    guessWords = newWordSession.guesses,
                                    falseKeyboardKeys = state.value.falseKeyboardKeys
                                ),
                            )
                        }
                    } else {
                        val updatedGuesses = requireWordSession.guesses.toMutableList().apply {
                            this[index] = incorrectGuessWord
                            this[index + 1] = requireWordSession.guesses[index + 1].copy(
                                state = GuessWordState.Editing
                            )
                        }.toImmutableList()

                        val newWordSession = runItThroughThePipes(
                            requireWordSession.copy(
                                guesses = updatedGuesses
                            )
                        )

                        _state.update {
                            it.copy(
                                gameTitleMessage = gameTitleMessage,
                                wordRowAnimating = true,
                                wordSession = newWordSession,
                                falseKeyboardKeys = getUpdatedFalseKeyboardKeys(
                                    guessWords = newWordSession.guesses,
                                    falseKeyboardKeys = state.value.falseKeyboardKeys
                                ),
                            )
                        }
                    }
                }

                WordGameValidationResultType.Success -> {
                    val gameTitleMessage = GameTitleMessage(
                        message = result.textRes,
                        isError = false
                    )
                    val newWordSession = runItThroughThePipes(
                        requireWordSession.copy(
                            guesses = getUpdatedWordRows(
                                index = index,
                                guessWord = requireWordSession.guesses[index]
                                    .lockInGuess(state.value.mysteryWord.word, isFinalGuess(index))
                            ),
                            state = WordSessionState.Success
                        )
                    )
                    _state.update {
                        it.copy(
                            wordRowAnimating = true,
                            wordSession = newWordSession,
                            gameTitleMessage = gameTitleMessage,
                            falseKeyboardKeys = getUpdatedFalseKeyboardKeys(
                                guessWords = newWordSession.guesses,
                                falseKeyboardKeys = state.value.falseKeyboardKeys
                            )
                        )
                    }
                }
            }
        }
    }

    private fun WordGameEvent.OnErrorAnimationFinished.onEvent() {
        if (state.value.gameState.isGameOver) {
            return
        }
        _state.update {
            it.copy(
                gameTitleMessage = GameTitleMessage(
                    message = TextRes.StringRes(Res.string.what_in_da_word)
                )
            )
        }
    }

    private fun isFinalGuess(index: Int): Boolean = index + 1 == requireWordSession.guesses.size

    private fun eraseLetter(index: Int) {
        when (val result = requireWordSession.guesses[index].eraseLetter()) {
            is Option.Error -> {
                _state.update {
                    it.copy(
                        gameTitleMessage = GameTitleMessage(
                            message = result.textRes,
                            isError = true
                        )
                    )
                }
            }

            is Option.Success -> {
                _state.update {
                    it.copy(
                        wordSession = requireWordSession.copy(
                            guesses = getUpdatedWordRows(index, result.data)
                        )
                    )
                }
            }
        }
    }

    private fun getUpdatedWordRows(index: Int, guessWord: GuessWord): ImmutableList<GuessWord> {
        val mut = mutableListOf<GuessWord>()
        mut.addAll(requireWordSession.guesses)
        mut[index] = guessWord
        return mut.toImmutableList()
    }

    private fun getCurrentGuessWordIndexAndHandleError(): Int? {
        val index = requireWordSession.guesses.indexOfFirst {
            it.state == GuessWordState.Editing
        }
        return if (index == -1) {
            _state.update {
                it.copy(
                    gameTitleMessage = GameTitleMessage(
                        message = TextRes.StringRes(Res.string.no_more_guess_attempts_left),
                        isError = true
                    )
                )
            }
            null
        } else {
            index
        }
    }

    private suspend fun runItThroughThePipes(wordSession: WordSession): WordSession {
        gameRepository.upsertWordSession(wordSession)
        return when (wordSession.gameMode) {
            GameMode.Daily -> gameRepository.getOrCreateWordSessionByDate(
                language = wordSession.language,
                gameMode = wordSession.gameMode,
                mysteryWord = wordSession.mysteryWord,
                wordLength = wordSession.mysteryWord.length,
                maxAttempts = wordSession.maxAttempts
            )

            GameMode.Infinity -> gameRepository.getOrCreateWordSessionInfinityMode(
                language = wordSession.language,
                mysteryWord = wordSession.mysteryWord,
                wordLength = wordSession.mysteryWord.length,
                maxAttempts = wordSession.maxAttempts
            )
        }
    }
}