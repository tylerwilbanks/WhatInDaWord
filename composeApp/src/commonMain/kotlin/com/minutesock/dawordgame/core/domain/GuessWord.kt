package com.minutesock.dawordgame.core.domain

import com.minutesock.dawordgame.core.data.DbEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.Instant

data class GuessWord(
    val letters: ImmutableList<GuessLetter>,
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