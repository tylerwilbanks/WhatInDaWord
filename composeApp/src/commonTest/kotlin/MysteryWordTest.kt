import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.di.testDbModule
import com.minutesock.dawordgame.feature.game.data.GameRepository
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.atTime
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class MysteryWordTest {

    private val testDispatcher = StandardTestDispatcher()
    private val koin = startKoin {
        modules(
            testDbModule(testDispatcher)
        )
    }.koin

    private val gameRepository = GameRepository(
        validWordDataSource = koin.get(),
        wordSelectionDataSource = koin.get(),
        defaultDispatcher = testDispatcher
    )

    @BeforeTest
    fun setup() = runTest(testDispatcher) {
        val gameLanguage = GameLanguage.English
        gameRepository.setupWords(gameLanguage)
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun wordOfTheDayIsSame() = runTest(testDispatcher) {
        val gameLanguage = GameLanguage.English
        val earlyTime =
            LocalDate(year = 2000, month = Month.MAY, dayOfMonth = 1).atTime(hour = 1, minute = 1)
        val lateTime =
            LocalDate(year = 2000, month = Month.MAY, dayOfMonth = 1).atTime(hour = 22, minute = 1)
        val earlyMysteryWord =
            gameRepository.selectMysteryWordByDate(gameLanguage = gameLanguage, date = earlyTime)
        val lateMysteryWord =
            gameRepository.selectMysteryWordByDate(gameLanguage = gameLanguage, date = lateTime)
        assertEquals(earlyMysteryWord, lateMysteryWord)
    }

    @Test
    fun wordOfTheDayIsDifferent() = runTest(testDispatcher) {
        val gameLanguage = GameLanguage.English
        val pastTime =
            LocalDate(year = 2000, month = Month.MAY, dayOfMonth = 1).atTime(hour = 1, minute = 1)
        val currentTime =
            LocalDate(year = 2000, month = Month.MAY, dayOfMonth = 2).atTime(hour = 22, minute = 1)
        val yesterdayWord =
            gameRepository.selectMysteryWordByDate(gameLanguage = gameLanguage, date = pastTime)
        val todayWord =
            gameRepository.selectMysteryWordByDate(gameLanguage = gameLanguage, date = currentTime)
        assertNotEquals(yesterdayWord, todayWord)
    }
}