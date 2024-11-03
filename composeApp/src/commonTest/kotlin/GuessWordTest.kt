import com.minutesock.dawordgame.core.domain.GuessLetter
import com.minutesock.dawordgame.core.domain.GuessLetterState
import com.minutesock.dawordgame.core.domain.GuessWord
import com.minutesock.dawordgame.core.domain.GuessWordState
import kotlinx.collections.immutable.toImmutableList
import kotlin.test.Test
import kotlin.test.assertEquals

class GuessWordTest {

    @Test
    fun duplicateCharacters_singleIncorrectPositionBeforeCorrectPosition() {
        val correctWord = "cedar"
        val guessWord = "radar"
        val userGuessWord = GuessWord(
            letters = List(5) {
                GuessLetter(guessWord[it], GuessLetterState.Unknown)
            }.toImmutableList(),
            state = GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord, false)
        assertEquals(
            GuessLetterState.Absent,
            updatedGuessWord.letters[0].state
        )
        assertEquals(
            GuessLetterState.Absent,
            updatedGuessWord.letters[1].state
        )
        assertEquals(
            GuessLetterState.Correct,
            updatedGuessWord.letters[2].state
        )
        assertEquals(
            GuessLetterState.Correct,
            updatedGuessWord.letters[3].state
        )
        assertEquals(
            GuessLetterState.Correct,
            updatedGuessWord.letters[4].state
        )
    }

    @Test
    fun duplicateCharacters_multipleIncorrectPositionsBeforeCorrectPosition() {
        val correctWord = "razor"
        val guessWord = "error"
        val userGuessWord = GuessWord(
            letters = List(5) {
                GuessLetter(guessWord[it], GuessLetterState.Unknown)
            }.toImmutableList(),
            state = GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord, false)
        assertEquals(
            GuessLetterState.Absent,
            updatedGuessWord.letters[0].state
        )
        assertEquals(
            GuessLetterState.Present,
            updatedGuessWord.letters[1].state
        )
        assertEquals(
            GuessLetterState.Absent,
            updatedGuessWord.letters[2].state
        )
        assertEquals(
            GuessLetterState.Correct,
            updatedGuessWord.letters[3].state
        )
        assertEquals(
            GuessLetterState.Correct,
            updatedGuessWord.letters[4].state
        )
    }

    @Test
    fun duplicateCharacters_singleIncorrectPositionAfterCorrectPosition() {
        val correctWord = "maxim"
        val guessWord = "magma"
        val userGuessWord = GuessWord(
            letters = List(5) {
                GuessLetter(guessWord[it], GuessLetterState.Unknown)
            }.toImmutableList(),
            state = GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord, false)
        assertEquals(
            GuessLetterState.Correct,
            updatedGuessWord.letters[0].state
        )
        assertEquals(
            GuessLetterState.Correct,
            updatedGuessWord.letters[1].state
        )
        assertEquals(
            GuessLetterState.Absent,
            updatedGuessWord.letters[2].state
        )
        assertEquals(
            GuessLetterState.Present,
            updatedGuessWord.letters[3].state
        )
        assertEquals(
            GuessLetterState.Absent,
            updatedGuessWord.letters[4].state
        )
    }

    @Test
    fun duplicateCharacters_multipleIncorrectPositionsBeforeAndAfterCorrectPosition() {
        val correctWord = "maxim"
        val guessWord = "mummy"
        val userGuessWord = GuessWord(
            letters = List(5) {
                GuessLetter(guessWord[it], GuessLetterState.Unknown)
            }.toImmutableList(),
            state = GuessWordState.Editing
        )
        val updatedGuessWord = userGuessWord.lockInGuess(correctWord, false)
        assertEquals(
            GuessLetterState.Correct,
            updatedGuessWord.letters[0].state
        )
        assertEquals(
            GuessLetterState.Absent,
            updatedGuessWord.letters[1].state
        )
        assertEquals(
            GuessLetterState.Present,
            updatedGuessWord.letters[2].state
        )
        assertEquals(
            GuessLetterState.Absent,
            updatedGuessWord.letters[3].state
        )
        assertEquals(
            GuessLetterState.Absent,
            updatedGuessWord.letters[4].state
        )
    }
}