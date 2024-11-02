package com.minutesock.dawordgame.core.domain

import com.minutesock.dawordgame.core.data.DbEntity
import com.minutesock.dawordgame.core.util.Option
import com.minutesock.dawordgame.feature.game.presentation.GuessWordError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class GuessWord(
    val letters: ImmutableList<GuessLetter>,
    val state: GuessWordState,
    val errorState: GuessWordError = GuessWordError.None,
    val completeTime: Instant? = null,
    override val id: Long = 0,
    val sessionId: Long = 0
) : DbEntity {
    val word: String get() = letters.joinToString("") { it.displayCharacter }.lowercase()
    val displayWord: String get() = letters.joinToString("") { it.displayCharacter }.uppercase()
    val isIncomplete: Boolean get() = letters.any { it.availableForInput }

    fun addGuessLetter(guessLetter: GuessLetter): Option<GuessWord?> {
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
                character = guessLetter.character,
                state = guessLetter.state
            )
        }
        return Option.Success(
            data = this.copy(
                letters = newGuessLetterList.toImmutableList(),
                state = this.state
            )
        )
    }

    fun eraseLetter(): Option<GuessWord?> {
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

    fun updateState(newState: GuessWordState): GuessWord {
        return this.copy(
            letters = this.letters,
            state = newState
        )
    }

    private fun updateStateAfterGuess(
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

    fun lockInGuess(correctWord: String, isFinalGuess: Boolean): GuessWord {
        val newGuessLetterItems = mutableListOf<GuessLetter>()
        val correctChars: List<Char> = correctWord.map { it.lowercaseChar() }

        this.letters.forEachIndexed { index: Int, guessLetter: GuessLetter ->
            val newState = when {
                guessLetter.character == correctChars[index] -> GuessLetterState.Correct
                correctChars.contains(guessLetter.character) -> GuessLetterState.Present
                else -> GuessLetterState.Absent
            }
            newGuessLetterItems.add(
                GuessLetter(
                    character = guessLetter.character,
                    state = newState
                )
            )
        }

        //clean up any extra present letters
        this.letters.forEachIndexed { index, userGuessLetter ->
            val correctDuplicateLetterCount = correctChars.count { it == userGuessLetter.character }
            val currentPresentAndCorrectLetterCount = newGuessLetterItems.count {
                it.character == this.letters[index].character && it.state == GuessLetterState.Correct ||
                        it.character == this.letters[index].character && it.state == GuessLetterState.Present
            }

            if (newGuessLetterItems[index].state == GuessLetterState.Present && currentPresentAndCorrectLetterCount > correctDuplicateLetterCount) {
                newGuessLetterItems[index] = newGuessLetterItems[index].copy(
                    state = GuessLetterState.Absent
                )
            }

        }

        return this.copy(
            letters = newGuessLetterItems.toImmutableList(),
            state = updateStateAfterGuess(correctWord, isFinalGuess),
            completeTime = Clock.System.now()
        )
    }
}

enum class GuessWordState {
    Unused,
    Editing,
    Complete,
    Correct,
    Failure
}