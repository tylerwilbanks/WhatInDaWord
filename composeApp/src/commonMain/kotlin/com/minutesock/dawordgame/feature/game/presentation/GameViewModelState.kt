package com.minutesock.dawordgame.feature.game.presentation

import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.getSystemLanguage

data class GameViewModelState(
    val gameLanguage: GameLanguage = getSystemLanguage(),
    val validWordCount: Long = 0,
    val wordSelectionCount: Long = 0
)