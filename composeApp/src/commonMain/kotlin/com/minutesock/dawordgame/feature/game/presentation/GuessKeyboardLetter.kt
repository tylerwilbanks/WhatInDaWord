package com.minutesock.dawordgame.feature.game.presentation

import androidx.compose.ui.graphics.Color
import com.minutesock.dawordgame.core.domain.LetterState
import com.minutesock.dawordgame.core.theme.guessLetterGreen
import com.minutesock.dawordgame.core.theme.guessLetterYellow

data class GuessKeyboardLetter(
    val keyName: String,
    val state: LetterState = LetterState.Unknown,
    val character: Char = keyName.first()
) {
    fun displayColor(defaultColor: Color) = when (state) {
        LetterState.Unknown -> defaultColor
        LetterState.Absent -> defaultColor.copy(alpha = 0.25f)
        LetterState.Present -> guessLetterYellow
        LetterState.Correct -> guessLetterGreen
    }
}