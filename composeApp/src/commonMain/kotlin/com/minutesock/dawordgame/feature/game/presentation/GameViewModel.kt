package com.minutesock.dawordgame.feature.game.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun setupGame(gameMode: GameMode) {
        viewModelScope.launch {
            val gameLanguage = getSystemLanguage()
            gameRepository.setupWords(gameLanguage)
            val selectedWord = when (gameMode) {
                GameMode.Daily -> gameRepository.selectMysteryWordByDate(gameLanguage)
                GameMode.Infinity -> gameRepository.selectMysteryWord(gameLanguage)
            }
            _state.update {
                it.copy(
                    gameLanguage = gameLanguage,
                    mysteryWord = selectedWord,
                    gameMode = gameMode
                )
            }
        }
    }
}