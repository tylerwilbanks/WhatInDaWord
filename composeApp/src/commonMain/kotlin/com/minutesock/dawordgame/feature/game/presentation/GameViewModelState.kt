package com.minutesock.dawordgame.feature.game.presentation

import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.WordSelection
import com.minutesock.dawordgame.core.domain.WordSession
import com.minutesock.dawordgame.core.domain.WordSessionState

data class GameViewModelState(
    val loadingState: GameLoadingState = GameLoadingState.Idle,
    val mysteryWord: WordSelection = WordSelection(
        id = -1,
        word = "",
        language = GameLanguage.English
    ),
    val wordSession: WordSession? = null,
    val wordRowAnimating: Boolean = false,
    val falseKeyboardKeys: FalseKeyboardKeys = FalseKeyboardKeys(),
) {
    val gameMode get() = wordSession?.gameMode ?: GameMode.Daily
    val language get() = wordSession?.language ?: GameLanguage.English
    val gameState get() = wordSession?.state ?: WordSessionState.NotStarted
}

enum class GameLoadingState {
    Idle,
    Loading,
    Loaded
}
