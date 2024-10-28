package com.minutesock.dawordgame.feature.game.presentation

import androidx.compose.ui.graphics.Color
import com.minutesock.dawordgame.core.domain.GuessLetterState
import com.minutesock.dawordgame.core.theme.guessLetterGreen
import com.minutesock.dawordgame.core.theme.guessLetterYellow

data class GuessKeyboardLetter(
    val keyName: String,
    val state: GuessLetterState = GuessLetterState.Unknown,
    val character: Char = keyName.first()
) {
    fun displayColor(defaultColor: Color) = when (state) {
        GuessLetterState.Unknown -> defaultColor
        GuessLetterState.Absent -> defaultColor.copy(alpha = 0.25f)
        GuessLetterState.Present -> guessLetterYellow
        GuessLetterState.Correct -> guessLetterGreen
    }
}