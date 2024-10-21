package com.minutesock.dawordgame.feature.game.presentation

import androidx.compose.ui.graphics.Color
import com.minutesock.dawordgame.core.domain.LetterState
import com.minutesock.dawordgame.core.theme.guessLetterGreen
import com.minutesock.dawordgame.core.theme.guessLetterYellow

data class GuessLetterItem(
    private val _character: Char = AVAILABLE_CHAR,
    val letterState: LetterState = LetterState.Unknown
) {
    val displayCharacter get() = _character.toString().uppercase()
    val character get() = _character.lowercaseChar()
    val availableForInput get() = _character == AVAILABLE_CHAR
    val answered get() = _character != AVAILABLE_CHAR

    fun displayColor(absentBackgroundColor: Color) = when (letterState) {
        LetterState.Unknown -> absentBackgroundColor
        LetterState.Absent -> absentBackgroundColor
        LetterState.Present -> guessLetterYellow
        LetterState.Correct -> guessLetterGreen
    }

    fun erase(): GuessLetterItem {
        return this.copy(
            _character = AVAILABLE_CHAR
        )
    }

    companion object {
        const val AVAILABLE_CHAR = ' '
    }
}