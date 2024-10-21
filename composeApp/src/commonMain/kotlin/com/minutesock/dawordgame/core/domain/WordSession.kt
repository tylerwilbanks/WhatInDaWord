package com.minutesock.dawordgame.core.domain

import kotlinx.datetime.Instant

data class WordSession(
    val id: Long = 0,
    val guesses: List<GuessWord>,
    val startTime: Instant,
    val endTime: Instant
)

data class GuessWord(
    val letters: List<GuessLetter>,
    val state: GuessWordState,
    val completeTime: Instant
)

enum class GuessWordState {
    Unused,
    Editing,
    Complete,
    Correct,
    Failure
}

data class GuessLetter(
    val character: Char,
    val state: LetterState
)
