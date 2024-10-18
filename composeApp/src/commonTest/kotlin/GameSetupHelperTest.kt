import com.minutesock.dawordgame.core.data.ValidWordDataSource
import com.minutesock.dawordgame.core.data.WordSelectionDataSource
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.di.testDbModule
import com.minutesock.dawordgame.feature.game.GameSetupHelper
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals


class GameSetupHelperTest {
    private val testDispatcher = StandardTestDispatcher()
    private val koin = startKoin {
        modules(testDbModule(testDispatcher))
    }.koin

    private val validWordDataSource: ValidWordDataSource = koin.get()
    private val wordSelectionDataSource: WordSelectionDataSource = koin.get()

    private val gameSetupHelper =
        GameSetupHelper(
            validWordDataSource = validWordDataSource,
            wordSelectionDataSource = wordSelectionDataSource,
            defaultDispatcher = testDispatcher
        )

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun english_validWordsAreInsertedIntoDb() = runTest(testDispatcher) {
        val gameLanguage = GameLanguage.English
        assertEquals(0, validWordDataSource.getValidWordCount())
        gameSetupHelper.upsertValidWordsIfNeeded(gameLanguage)
        assertEquals(gameLanguage.expectedValidWordCount, validWordDataSource.getValidWordCount())
    }


    @Test
    fun english_wordSelectionsAreInsertedIntoDb() = runTest(testDispatcher) {
        val gameLanguage = GameLanguage.English
        assertEquals(0, wordSelectionDataSource.getWordSelectionCount())
        gameSetupHelper.upsertWordSelectionIfNeeded(gameLanguage)
        assertEquals(
            gameLanguage.expectedWordSelectionCount,
            wordSelectionDataSource.getWordSelectionCount()
        )
    }
}