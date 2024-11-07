import com.minutesock.dawordgame.core.data.DbClient
import com.minutesock.dawordgame.core.data.guessletter.GuessLetterDataSource
import com.minutesock.dawordgame.core.data.guessword.GuessWordDataSource
import com.minutesock.dawordgame.core.data.wordsession.WordSessionDataSource
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.di.KoinProvider
import com.minutesock.dawordgame.di.initKoinForTesting
import com.minutesock.dawordgame.di.testDbModule
import com.minutesock.dawordgame.feature.game.data.GameRepository
import com.minutesock.dawordgame.feature.game.domain.GuessWordValidator
import com.minutesock.dawordgame.feature.game.presentation.GameViewModel
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
        assertEquals(6, guessWordDataSource.getCount())
        assertEquals(wordLength.toLong() * attempts, guessLetterDataSource.getCount())
    }
}