package com.minutesock.dawordgame.feature.dictionary.presentation

import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.GuessWord
import com.minutesock.dawordgame.core.domain.WordSession
import com.minutesock.dawordgame.core.domain.WordSessionState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class DictionaryWordSessionInfo(
    val displayDate: String,
    val wordRows: ImmutableList<DictionaryGuessWordRow>,
    val displayCompleteTime: String,
    val gameMode: GameMode,
    val sessionState: WordSessionState
)

data class DictionaryGuessWordRow(
    val guessWord: GuessWord,
    val displayTimestamp: String
)

fun WordSession.toDictionaryWordSessionInfo(): DictionaryWordSessionInfo {
    return DictionaryWordSessionInfo(
        displayDate = formattedTime?.toString() ?: "",
        wordRows = guesses.mapIndexed { index: Int, guessWord: GuessWord ->
            DictionaryGuessWordRow(
                guessWord = guessWord,
                displayTimestamp = getFormattedIndividualElapsedTime(index)
            )
        }.toImmutableList(),
        displayCompleteTime = formattedElapsedTime,
        gameMode = gameMode,
        sessionState = state
    )
}
