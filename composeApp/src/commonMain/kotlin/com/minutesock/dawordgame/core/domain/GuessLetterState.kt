package com.minutesock.dawordgame.core.domain

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