package com.minutesock.dawordgame.feature.game.presentation

import androidx.compose.ui.graphics.Color
import com.minutesock.dawordgame.core.domain.GuessLetterState
import com.minutesock.dawordgame.core.theme.guessLetterAbsentDark
import com.minutesock.dawordgame.core.theme.guessLetterAbsentLight
import com.minutesock.dawordgame.core.theme.guessLetterGreen
import com.minutesock.dawordgame.core.theme.guessLetterYellow

data class GuessKeyboardLetter(
    val keyName: String,
    val state: GuessLetterState = GuessLetterState.Unknown,
    val character: Char = keyName.first()
) {
    fun displayColor(darkTheme: Boolean, defaultColor: Color) = when (state) {
        GuessLetterState.Unknown -> defaultColor
        GuessLetterState.Absent -> if (darkTheme) guessLetterAbsentDark else guessLetterAbsentLight
        GuessLetterState.Present -> guessLetterYellow
        GuessLetterState.Correct -> guessLetterGreen
    }
}