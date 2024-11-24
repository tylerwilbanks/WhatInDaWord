package com.minutesock.dawordgame.feature.game.presentation

import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.WordSelection
import com.minutesock.dawordgame.core.domain.WordSession
import com.minutesock.dawordgame.core.domain.WordSessionState
import com.minutesock.dawordgame.core.uiutil.TextRes
import whatindaword.composeapp.generated.resources.Res
import whatindaword.composeapp.generated.resources.what_in_da_word

data class GameViewModelState(
    val gameTitleMessage: GameTitleMessage = GameTitleMessage(),
    val loadingState: GameLoadingState = GameLoadingState.Idle,
    val mysteryWord: WordSelection = WordSelection(
        id = -1,
        word = "",
        language = GameLanguage.English
    ),
    val wordSession: WordSession? = null,
    val language: GameLanguage = wordSession?.language ?: GameLanguage.English,
    val wordRowAnimating: Boolean = false,
    val falseKeyboardKeys: FalseKeyboardKeys = FalseKeyboardKeys(),
    val screenState: GameScreenState = GameScreenState.Game
) {

    val gameState get() = wordSession?.state ?: WordSessionState.NotStarted
    val gameMode get() = wordSession?.gameMode ?: GameMode.Daily
}

data class GameTitleMessage(
    val message: TextRes = TextRes.StringRes(Res.string.what_in_da_word),
    val isError: Boolean = false
)

enum class GameLoadingState {
    Idle,
    Loading,
    Loaded
}

enum class GameScreenState {
    Game,
    Stats
}
