package com.minutesock.dawordgame.core.domain

import com.minutesock.dawordgame.core.data.DbEntity

data class GuessLetter(
    val character: Char,
    val state: GuessLetterState,
    override val id: Long = 0,
    val guessWordId: Long = 0,
) : DbEntity {
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