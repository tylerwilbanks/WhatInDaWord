package com.minutesock.dawordgame.feature.game.presentation

import com.minutesock.dawordgame.core.domain.GameLanguage

data class GameViewModelState(
    val gameLanguage: GameLanguage = GameLanguage.English,
    val validWordCount: Long = 0,
    val wordSelectionCount: Long = 0
)