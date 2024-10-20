package com.minutesock.dawordgame.feature.game.presentation

import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.WordSelection
import com.minutesock.dawordgame.getSystemLanguage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class GameViewModelState(
    val gameLanguage: GameLanguage = getSystemLanguage(),
    val mysteryWord: WordSelection = WordSelection(
        id = -1,
        word = "",
        language = GameLanguage.English
    ),
    val gameMode: GameMode = GameMode.Daily,
    val guessWords: ImmutableList<GuessWord> =
        persistentListOf(
            GuessWord(
                letters = persistentListOf(
                    GuessLetter('c'),
                    GuessLetter('h'),
                    GuessLetter('u'),
                    GuessLetter('m'),
                    GuessLetter('b')
                )
            )
        ), // persistentListOf(),
    val wordRowAnimating: Boolean = false,
)

enum class GameMode {
    Daily,
    Infinity
}