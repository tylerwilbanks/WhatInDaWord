package com.minutesock.dawordgame.feature.game.domain

import com.minutesock.dawordgame.core.data.ValidWordDataSource
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.GuessWord
import com.minutesock.dawordgame.core.domain.WordSessionState
import com.minutesock.dawordgame.core.uiutil.TextRes
import com.minutesock.dawordgame.di.KoinProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import whatindaword.composeapp.generated.resources.Res
import whatindaword.composeapp.generated.resources.unknown_error
import whatindaword.composeapp.generated.resources.what_in_da_word
import whatindaword.composeapp.generated.resources.word_does_not_exist
import whatindaword.composeapp.generated.resources.word_is_incomplete

enum class WordGameValidationResultType {
    Unknown,
    Error,
    Incorrect,
    Success
}

data class WordGameValidationResult(
    val type: WordGameValidationResultType,
    val textRes: TextRes
)

class GuessWordValidator(
    private val validWordDataSource: ValidWordDataSource = KoinProvider.instance.get(),
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val failureMessages = listOf(
        "The word eludes you this time.",
        "A valiant effort, but not the word.",
        "The word remains a mystery.",
        "The word stays hidden.",
        "The word is a sly one.",
        "Once more, the word dances out of your sight.",
        "The word prefers to remain in the shadows."
    ).shuffled()

    private val encouragingMessages = listOf(
        "Not quite!",
        "The right word is out there!",
        "You're learning from each attempt!",
        "Keep the guesses flowing.",
        "Success is built on persistence.",
        "Embrace the challenge.",
        "Mistakes fuel learning.",
        "Guess by guess!",
        "Closer with each attempt!",
        "Resilience: your compass to word victory!",
        "You're a tenacious one."
    ).shuffled()

    private val correctMessages = listOf(
        "Well guessed!",
        "Marvelous.",
        "Exceptional.",
        "Champion.",
        "You cracked it!",
        "Absolutely right.",
        "Astonishing.",
        "Glorious!",
        "On the nose!"
    ).shuffled()

    private var failureMessageIndex = 0
    private var encouragingMessageIndex = 0
    private var correctMessagesIndex = 0

    private fun getRandomMessage(messagePool: List<String>, index: Int): Pair<String, Int> {
        val i = if (index + 1 == messagePool.size) 0 else index + 1
        return Pair(messagePool[i], i)
    }

    fun obtainRandomMessageBasedOnGameState(gameState: WordSessionState): TextRes {
        return when (gameState) {
            WordSessionState.NotStarted -> TextRes.StringRes(Res.string.what_in_da_word)
            WordSessionState.InProgress -> {
                TextRes.Raw(getRandomMessage(encouragingMessages, encouragingMessageIndex).first)
            }

            WordSessionState.Success -> {
                TextRes.Raw(getRandomMessage(correctMessages, correctMessagesIndex).first)
            }

            WordSessionState.Failure -> {
                TextRes.Raw(getRandomMessage(failureMessages, failureMessageIndex).first)
            }
        }
    }

    suspend fun validateGuess(
        guessWord: GuessWord,
        correctWord: String,
        isFinalGuess: Boolean,
        language: GameLanguage
    ): WordGameValidationResult {
        return withContext(defaultDispatcher) {
            if (guessWord.isIncomplete) {
                return@withContext WordGameValidationResult(
                    WordGameValidationResultType.Error,
                    TextRes.StringRes(Res.string.word_is_incomplete)
                )
            }

            if (guessWord.word == correctWord) {
                val randomMessageResult = getRandomMessage(correctMessages, correctMessagesIndex)
                correctMessagesIndex = randomMessageResult.second
                return@withContext WordGameValidationResult(
                    WordGameValidationResultType.Success,
                    TextRes.Raw(randomMessageResult.first)
                )
            }

            val validWordContainsGuessWord = validWordDataSource.select(guessWord.word, language) != null

            if (!validWordContainsGuessWord) {
                return@withContext WordGameValidationResult(
                    WordGameValidationResultType.Error,
                    TextRes.StringRes(Res.string.word_does_not_exist)
                )
            }

            if (guessWord.word != correctWord && validWordContainsGuessWord) {
                val (message, newIndex) = getRandomMessage(
                    if (isFinalGuess) failureMessages else encouragingMessages,
                    if (isFinalGuess) failureMessageIndex else encouragingMessageIndex
                )
                if (isFinalGuess) {
                    failureMessageIndex = newIndex
                } else {
                    encouragingMessageIndex = newIndex
                }

                return@withContext WordGameValidationResult(
                    WordGameValidationResultType.Incorrect,
                    TextRes.Raw(message)
                )
            }

            return@withContext WordGameValidationResult(
                WordGameValidationResultType.Unknown,
                TextRes.StringRes(Res.string.unknown_error)
            )
        }
    }
}