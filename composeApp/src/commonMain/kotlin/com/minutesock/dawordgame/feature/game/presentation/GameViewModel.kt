package com.minutesock.dawordgame.feature.game.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.dawordgame.feature.game.data.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameViewModelState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            gameRepository.setupWords(gameLanguage = state.value.gameLanguage)
        }
    }
}