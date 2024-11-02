package com.minutesock.dawordgame.core.domain

import androidx.compose.ui.graphics.Color
import com.minutesock.dawordgame.core.data.DbEntity
import com.minutesock.dawordgame.core.theme.guessLetterGreen
import com.minutesock.dawordgame.core.theme.guessLetterYellow

data class GuessLetter(
    val character: Char,
    val state: GuessLetterState,
    override val id: Long = 0,
    val guessWordId: Long = 0,
) : DbEntity {
    val displayCharacter get() = character.toString().uppercase()
    val availableForInput get() = character == AVAILABLE_CHAR
    val answered get() = character != AVAILABLE_CHAR

    fun displayColor(absentBackgroundColor: Color) = when (state) {
        GuessLetterState.Unknown -> absentBackgroundColor
        GuessLetterState.Absent -> absentBackgroundColor
        GuessLetterState.Present -> guessLetterYellow
        GuessLetterState.Correct -> guessLetterGreen
    }

    fun erase(): GuessLetter {
        return this.copy(
            character = AVAILABLE_CHAR,
            state = state
        )
    }

    companion object {
        const val AVAILABLE_CHAR = ' '
    }
}

enum class GuessLetterState {
    Unknown,
    Absent,
    Present,
    Correct;

    val emoji: String
        get() {
            return when (this) {
                Unknown -> "⬛"
                Absent -> "⬛"
                Present -> "🟨"
                Correct -> "🟩"
            }
        }
}