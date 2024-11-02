package com.minutesock.dawordgame.feature.game.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.GuessWordState
import com.minutesock.dawordgame.core.uiutil.TextRes
import com.minutesock.dawordgame.feature.game.data.GameRepository
import com.minutesock.dawordgame.getSystemLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import whatindaword.composeapp.generated.resources.Res
import whatindaword.composeapp.generated.resources.no_more_guess_attempts_left

class GameViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameViewModelState())
    val state = _state.asStateFlow()

    private val requireWordSession = state.value.wordSession!!

    fun setupGame(gameMode: GameMode, wordLength: Int = 5, attempts: Int = 6) {
        viewModelScope.launch {
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

            // todo-tyler update false keyboard keys here
            _state.update {
                it.copyWithWordSession(
                    wordSession = wordSession,
                    loadingState = GameLoadingState.Loaded,
                    mysteryWord = selectedWord
                )
            }
        }
    }

    fun onEvent(event: WordGameEvent) {
        viewModelScope.launch {
            when (event) {
                WordGameEvent.OnAnsweredWordRowAnimationFinished -> TODO()
                is WordGameEvent.OnCharacterPress -> event.onEvent()
                WordGameEvent.OnCompleteAnimationFinished -> TODO()
                WordGameEvent.OnDeletePress -> TODO()
                WordGameEvent.OnEnterPress -> TODO()
                WordGameEvent.OnErrorAnimationFinished -> TODO()
                WordGameEvent.OnStatsPress -> TODO()
            }
        }
    }

    private fun WordGameEvent.OnCharacterPress.onEvent() {
//        when (val result = getCurrentGuessWordIndex()) {
//            is Option.Error, is Option.Loading -> _state.update {
//                it.copy(
//                    gameTitleMessage = GameTitleMessage(
//                        message = result.textRes ?: TextRes.StringRes(Res.string.what_in_da_word),
//                        isError = true
//                    )
//                )
//            }
//            is Option.Success -> getCurrentGuessWordIndexAndHandleError()?.let { index: Int ->
//                updateCurrentGuessWord()
//            }
//        }
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

//    private fun updateCurrentGuessWord(index: Int, character: Char) {
//        val result = requireWordSession.guesses[index].addGuessLetter(
//            GuessLetter(character = character, state = GuessLetterState.Unknown)
//        )
//        when (result) {
//            is Option.Error -> {
//                result.uiText?.let { uiTextError ->
//                    result.errorCode?.let { errorCode ->
//                        _state.update {
//                            it.copy(
//                                guessWords = getUpdatedWordRows(
//                                    index, state.value.guessWords[index].copy(
//                                        errorState = GuessWordError.values()[errorCode]
//                                    )
//                                )
//                            )
//                        }
//
//                    }
//                    _state.update {
//                        it.copy(
//                            dailyWordStateMessage = DailyWordStateMessage(
//                                uiText = uiTextError,
//                                isError = true
//                            )
//                        )
//                    }
//                }
//
//            }
//
//            is Option.Success -> {
//                result.data?.let { newGuessWord ->
//                    _state.update {
//                        it.copy(
//                            guessWords = getUpdatedWordRows(index, newGuessWord)
//                        )
//                    }
//                }
//            }
//
//            else -> Unit
//        }
//    }


}