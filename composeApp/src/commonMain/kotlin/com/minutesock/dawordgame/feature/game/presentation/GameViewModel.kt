package com.minutesock.dawordgame.feature.game.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.feature.game.data.GameRepository
import com.minutesock.dawordgame.getSystemLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameViewModelState())
    val state = _state.asStateFlow()

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

            _state.update {
                it.copy(
                    loadingState = GameLoadingState.Loaded,
                    mysteryWord = selectedWord,
                    wordSession = wordSession
                )
            }
        }
    }

    fun onEvent(event: WordGameEvent) {
        viewModelScope.launch {
            when (event) {
                WordGameEvent.OnAnsweredWordRowAnimationFinished -> TODO()
                is WordGameEvent.OnCharacterPress -> TODO()
                WordGameEvent.OnCompleteAnimationFinished -> TODO()
                WordGameEvent.OnDeletePress -> TODO()
                WordGameEvent.OnEnterPress -> TODO()
                WordGameEvent.OnErrorAnimationFinished -> TODO()
                WordGameEvent.OnStatsPress -> TODO()
            }
        }
    }
}