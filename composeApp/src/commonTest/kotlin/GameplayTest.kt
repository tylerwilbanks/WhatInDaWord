import com.minutesock.dawordgame.core.data.DbClient
import com.minutesock.dawordgame.core.data.guessletter.GuessLetterDataSource
import com.minutesock.dawordgame.core.data.guessword.GuessWordDataSource
import com.minutesock.dawordgame.core.data.wordsession.WordSessionDataSource
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.GuessLetter
import com.minutesock.dawordgame.di.KoinProvider
import com.minutesock.dawordgame.di.initKoinForTesting
import com.minutesock.dawordgame.di.testDbModule
import com.minutesock.dawordgame.feature.game.data.GameRepository
import com.minutesock.dawordgame.feature.game.domain.GuessWordValidator
import com.minutesock.dawordgame.feature.game.presentation.GameViewModel
import com.minutesock.dawordgame.feature.game.presentation.WordGameEvent
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GameplayTest {
    private val testDispatcher = StandardTestDispatcher()
    private val koin = initKoinForTesting {
        modules(testDbModule(testDispatcher))
    }.koin

    private val dbClient: DbClient = KoinProvider.instance.get()
    private val gameRepository = GameRepository(
        defaultDispatcher = testDispatcher
    )

    private val guessWordValidator = GuessWordValidator(
        defaultDispatcher = testDispatcher
    )

    private val wordSessionDataSource: WordSessionDataSource = koin.get()
    private val guessWordDataSource: GuessWordDataSource = koin.get()
    private val guessLetterDataSource: GuessLetterDataSource = koin.get()

    @AfterTest
    fun teardown() = runTest(testDispatcher) {
        dbClient.clearDb()
        stopKoin()
    }

    @Test
    fun setupGame() = runTest(testDispatcher) {
        val gameViewModel = GameViewModel(
            gameRepository = gameRepository,
            guessWordValidator = guessWordValidator,
        )

        val wordLength = 5
        val attempts = 6

        gameViewModel.setupGame(
            gameMode = GameMode.Daily,
            wordLength = wordLength,
            attempts = attempts
        ).join()

        val wordSession = wordSessionDataSource.selectById(1)
        assertNotNull(wordSession)
        assertEquals(wordSession.id, gameViewModel.state.value.wordSession?.id)
        assertEquals(attempts.toLong(), guessWordDataSource.getCount())
        assertEquals(wordLength.toLong() * attempts, guessLetterDataSource.getCount())
    }

    @Test
    fun enterPress_wordSessionUpdatedCorrectlyInDatabase() = runTest(testDispatcher) {
        val gameViewModel = GameViewModel(
            gameRepository = gameRepository,
            guessWordValidator = guessWordValidator
        )

        val wordLength = 5
        val attempts = 6

        gameViewModel.setupGame(
            gameMode = GameMode.Daily,
            wordLength = wordLength,
            attempts = attempts
        ).join()

        val word = "green"
        word.map { it }.forEach {
            gameViewModel.onEvent(WordGameEvent.OnCharacterPress(it)).join()
        }

        gameViewModel.onEvent(WordGameEvent.OnEnterPress).join()
        val wordSessionId = 1L

        assertEquals(attempts.toLong(), guessWordDataSource.getCount())
        assertEquals(wordLength.toLong() * attempts, guessLetterDataSource.getCount())

        val wordSession = wordSessionDataSource.selectById(wordSessionId)
        assertNotNull(wordSession)

        assertEquals(attempts, wordSession.maxAttempts)
        assertEquals(attempts, wordSession.guesses.size)
        wordSession.guesses.first().let { firstGuess ->
            assertEquals(word[0], firstGuess.letters[0].character)
            assertEquals(word[1], firstGuess.letters[1].character)
            assertEquals(word[2], firstGuess.letters[2].character)
            assertEquals(word[3], firstGuess.letters[3].character)
            assertEquals(word[4], firstGuess.letters[4].character)
        }
        assertEquals(GuessLetter.Companion.AVAILABLE_CHAR, wordSession.guesses[1].letters[0].character)
    }
}