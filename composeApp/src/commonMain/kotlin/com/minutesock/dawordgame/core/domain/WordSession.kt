package com.minutesock.dawordgame.core.domain

import com.minutesock.dawordgame.core.data.DbEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

data class WordSession(
    val date: LocalDate,
    val mysteryWord: String,
    val language: GameLanguage,
    val maxAttempts: Int,
    val gameMode: GameMode,
    val state: WordSessionState,
    override val id: Long = 0,
    val guesses: ImmutableList<GuessWord> = persistentListOf(),
) : DbEntity {
    val endTime: Instant
        get() = TODO("Not implemented yet.")

    val wordLength = mysteryWord.length

    val completeTime = guesses.findLast { it.completeTime != null }?.completeTime

    val formattedTime = completeTime?.toLocalDateTime(TimeZone.currentSystemDefault())?.date

    val successTime =
        guesses.lastOrNull { it.completeTime != null && it.state == GuessWordState.Correct }?.completeTime

    val emojiRepresentation: String
        get() {
            val finalIndex =
                guesses.indexOfFirst { it.state == GuessWordState.Correct || it.state == GuessWordState.Failure }
            var text = ""
            guesses.forEachIndexed { index, guessWord ->
                if (index > finalIndex) {
                    return@forEachIndexed
                }
                guessWord.letters.forEach {
                    text += it.state.emoji
                }
                text += "\n"
            }
            return text.trimEnd('\n')
        }

    val shareText: String
        get() {
            val finalIndex =
                guesses.indexOfFirst { it.state == GuessWordState.Correct || it.state == GuessWordState.Failure }
            val resultLetter =
                if (finalIndex + 1 >= guesses.size && state == WordSessionState.Failure) "X" else "${finalIndex + 1}"

            return "What in da word!?\n" +
                    "Date: $date ${"\uD83D\uDCC5"}\n" +
                    "Mode: ${gameMode.name} ${gameMode.emoji}\n" +
                    "$resultLetter/${guesses.size}\n" +
                    "$formattedElapsedTime\n" +
                    emojiRepresentation
        }

    val elapsedTime: Duration
        get() {
            val firstGuess =
                guesses[0].state == GuessWordState.Correct || guesses[0].state == GuessWordState.Failure
            val incomplete =
                state == WordSessionState.NotStarted || state == WordSessionState.InProgress
            if (firstGuess || incomplete) {
                return Duration.ZERO
            }
            val startTime = guesses.find { it.completeTime != null }?.completeTime
            val endTime = guesses.findLast { it.completeTime != null }?.completeTime
            if (startTime != null && endTime != null) {
                return endTime.minus(startTime)
            }
            return Duration.INFINITE
        }

    fun getFormattedIndividualElapsedTime(currentIndex: Int): String {
        if (currentIndex == 0) {
            return "0m 0s"
        }

        val startTime = guesses[0].completeTime
        val currentTime = guesses[currentIndex].completeTime

        if (startTime != null && currentTime != null) {
            return currentTime.minus(startTime)
                .toComponents { minutes, seconds, _ ->
                    "${minutes}m ${seconds}s"
                }.toString()
        }
        return ""
    }

    val formattedElapsedTime: String
        get() = elapsedTime.toComponents { minutes, seconds, _ ->
            "${minutes}m ${seconds}s"
        }.toString()
}

enum class WordSessionState {
    NotStarted,
    InProgress,
    Success,
    Failure;

    val isGameOver
        get() = when (this) {
            Success, Failure -> true
            else -> false
        }
}
