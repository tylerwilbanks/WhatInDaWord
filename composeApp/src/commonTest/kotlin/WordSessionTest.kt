import com.minutesock.dawordgame.core.data.DbClient
import com.minutesock.dawordgame.core.data.guessletter.GuessLetter
import com.minutesock.dawordgame.core.data.wordsession.WordSessionDataSource
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.di.initKoin
import com.minutesock.dawordgame.di.testDbModule
import com.minutesock.dawordgame.feature.game.data.GameRepository
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class WordSessionTest {
    private val testDispatcher = StandardTestDispatcher()
    private val koin = initKoin {
        modules(testDbModule(testDispatcher))
    }.koin

    private val dbClient: DbClient = koin.get()
    private val wordSessionDataSource: WordSessionDataSource = koin.get()

    private val gameRepository = GameRepository(
        defaultDispatcher = testDispatcher
    )

    @BeforeTest
    fun setup() = runTest(testDispatcher) {
        dbClient.clearDb()
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun wordSessionDbInsertion() = runTest(testDispatcher) {
        val createdWordSession = gameRepository.getOrCreateWordSessionByDate(
            date = LocalDate(
                year = 2024,
                monthNumber = 10,
                dayOfMonth = 29
            ),
            language = GameLanguage.English,
            gameMode = GameMode.Daily,
            mysteryWord = "smack",
            maxAttempts = 6,
            wordLength = 5
        )
        val wordSessionFromDb = wordSessionDataSource.selectWordSessionEntity(createdWordSession.id)
        assertNotNull(wordSessionFromDb)
        assertEquals(1L, wordSessionFromDb.id)
    }

    @Test
    fun guessWordDbInsertion() = runTest(testDispatcher) {
        val createdWordSession = gameRepository.getOrCreateWordSessionByDate(
            date = LocalDate(
                year = 2024,
                monthNumber = 10,
                dayOfMonth = 29
            ),
            language = GameLanguage.English,
            gameMode = GameMode.Daily,
            mysteryWord = "smack",
            maxAttempts = 6,
            wordLength = 5
        )

        val wordSessionFromDb = wordSessionDataSource.selectWordSessionEntity(createdWordSession.id)

        assertNotNull(wordSessionFromDb)
        assertEquals(6, wordSessionFromDb.guesses.size)
    }

    @Test
    fun guessLetterDbInsertion() = runTest(testDispatcher) {
        val createdWordSession = gameRepository.getOrCreateWordSessionByDate(
            date = LocalDate(
                year = 2024,
                monthNumber = 10,
                dayOfMonth = 29
            ),
            language = GameLanguage.English,
            gameMode = GameMode.Daily,
            mysteryWord = "smack",
            maxAttempts = 6,
            wordLength = 5
        )

        val wordSessionFromDb = wordSessionDataSource.selectWordSessionEntity(createdWordSession.id)

        assertNotNull(wordSessionFromDb)
        assertEquals(5, wordSessionFromDb.guesses.first().letters.size)
        assertEquals(
            GuessLetter.AVAILABLE_CHAR,
            wordSessionFromDb.guesses.first().letters.first().character
        )
    }
}