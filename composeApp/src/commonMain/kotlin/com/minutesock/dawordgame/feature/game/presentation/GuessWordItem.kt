package com.minutesock.dawordgame.feature.game.presentation

import com.minutesock.dawordgame.core.data.guessword.GuessWordState
import com.minutesock.dawordgame.core.domain.GuessLetterState
import com.minutesock.dawordgame.core.util.Option
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class GuessWordItem(
    val letters: ImmutableList<GuessLetterItem>,
    val state: GuessWordState = GuessWordState.Unused,
    val errorState: GuessWordError = GuessWordError.None,
    val completeTime: Instant? = null,
) {
    val word: String get() = letters.joinToString("") { it.displayCharacter }.lowercase()
    val displayWord: String get() = letters.joinToString("") { it.displayCharacter }.uppercase()
    val isIncomplete: Boolean get() = letters.any { it.availableForInput }
}

fun GuessWordItem.addGuessLetter(guessLetterItem: GuessLetterItem): Option<GuessWordItem?> {
    val newGuessLetterList = this.letters.toMutableList()
    newGuessLetterList.indexOfFirst { it.availableForInput }.let { index ->
        if (index == -1) {
            val customError = GuessWordError.NoLettersAvailableForInput
            return Option.Error(
                textRes = customError.message,
                errorCode = customError.ordinal
            )
        }
        newGuessLetterList[index] = newGuessLetterList[index].copy(
            _character = guessLetterItem.character,
            guessLetterState = guessLetterItem.guessLetterState
        )
    }
    return Option.Success(
        data = this.copy(
            letters = newGuessLetterList.toImmutableList(),
            state = this.state
        )
    )
}

fun GuessWordItem.eraseLetter(): Option<GuessWordItem?> {
    val newGuessLetterList = this.letters.toMutableList()
    newGuessLetterList.indexOfLast { it.answered }.let { index ->
        if (index == -1) {
            val customError = GuessWordError.NoLettersToRemove
            return Option.Error(
                textRes = customError.message,
                errorCode = customError.ordinal
            )
        }

        newGuessLetterList[index] = newGuessLetterList[index].erase()

    }
    return Option.Success(
        data = this.copy(
            letters = newGuessLetterList.toImmutableList(),
            state = this.state
        )
    )
}

fun GuessWordItem.updateState(newState: GuessWordState): GuessWordItem {
    return this.copy(
        letters = this.letters,
        state = newState
    )
}

fun GuessWordItem.lockInGuess(correctWord: String, isFinalGuess: Boolean): GuessWordItem {
    val newGuessLetterItems = mutableListOf<GuessLetterItem>()
    val correctChars: List<Char> = correctWord.map { it.lowercaseChar() }

    this.letters.forEachIndexed { index: Int, guessLetterItem: GuessLetterItem ->
        val newState = when {
            guessLetterItem.character == correctChars[index] -> GuessLetterState.Correct
            correctChars.contains(guessLetterItem.character) -> GuessLetterState.Present
            else -> GuessLetterState.Absent
        }
        newGuessLetterItems.add(
            GuessLetterItem(
                _character = guessLetterItem.character,
                guessLetterState = newState
            )
        )
    }

    //clean up any extra present letters
    this.letters.forEachIndexed { index, userGuessLetter ->
        val correctDuplicateLetterCount = correctChars.count { it == userGuessLetter.character }
        val currentPresentAndCorrectLetterCount = newGuessLetterItems.count {
            it.character == this.letters[index].character && it.guessLetterState == GuessLetterState.Correct ||
                    it.character == this.letters[index].character && it.guessLetterState == GuessLetterState.Present
        }

        if (newGuessLetterItems[index].guessLetterState == GuessLetterState.Present && currentPresentAndCorrectLetterCount > correctDuplicateLetterCount) {
            newGuessLetterItems[index] = newGuessLetterItems[index].copy(
                guessLetterState = GuessLetterState.Absent
            )
        }

    }

    return this.copy(
        letters = newGuessLetterItems.toImmutableList(),
        state = updateStateAfterGuess(correctWord, isFinalGuess),
        completeTime = Clock.System.now()
    )
}

private fun GuessWordItem.updateStateAfterGuess(
    correctWord: String,
    isFinalGuess: Boolean
): GuessWordState {
    return when {
        this.word.lowercase() == correctWord.lowercase() -> GuessWordState.Correct
        !isFinalGuess && this.word.lowercase() != correctWord.lowercase() -> GuessWordState.Complete
        isFinalGuess && this.word.lowercase() != correctWord.lowercase() -> GuessWordState.Failure
        else -> GuessWordState.Complete
    }
}

