package com.minutesock.dawordgame.core.data.guessletter

import com.minutesock.dawordgame.core.data.DbEntity
import com.minutesock.dawordgame.core.domain.GuessLetterState

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