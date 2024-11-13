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
    val gameMode: GameMode = wordSession?.gameMode ?: GameMode.Daily,
    val language: GameLanguage = wordSession?.language ?: GameLanguage.English,
    val gameState: WordSessionState = wordSession?.state ?: WordSessionState.NotStarted,
    val wordRowAnimating: Boolean = false,
    val falseKeyboardKeys: FalseKeyboardKeys = FalseKeyboardKeys(),
    val screenState: GameScreenState = GameScreenState.Game
) {
    fun copyWithWordSession(
        wordSession: WordSession?,
        gameTitleMessage: GameTitleMessage = this.gameTitleMessage,
        loadingState: GameLoadingState = this.loadingState,
        mysteryWord: WordSelection = this.mysteryWord,
        wordRowAnimating: Boolean = this.wordRowAnimating,
        falseKeyboardKeys: FalseKeyboardKeys = this.falseKeyboardKeys
    ): GameViewModelState {
        return copy(
            wordSession = wordSession,
            gameTitleMessage = gameTitleMessage,
            loadingState = loadingState,
            mysteryWord = mysteryWord,
            wordRowAnimating = wordRowAnimating,
            falseKeyboardKeys = falseKeyboardKeys,
            gameMode = wordSession?.gameMode ?: GameMode.Daily,
            language = wordSession?.language ?: GameLanguage.English,
            gameState = wordSession?.state ?: WordSessionState.NotStarted
        )
    }
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
