package com.minutesock.dawordgame.core.data.guessword

import com.minutesock.dawordgame.core.data.DbEntity
import com.minutesock.dawordgame.core.data.guessletter.GuessLetter
import kotlinx.datetime.Instant

data class GuessWord(
    val letters: List<GuessLetter>,
    val state: GuessWordState,
    val completeTime: Instant? = null,
    override val id: Long = 0,
    val sessionId: Long = 0
) : DbEntity

enum class GuessWordState {
    Unused,
    Editing,
    Complete,
    Correct,
    Failure
}