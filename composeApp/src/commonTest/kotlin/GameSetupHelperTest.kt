import com.minutesock.dawordgame.core.data.DatabaseDriverFactory
import com.minutesock.dawordgame.core.data.DbClient
import com.minutesock.dawordgame.core.data.SqlDelightDbClient
import com.minutesock.dawordgame.core.data.SqlDelightValidWordDataSource
import com.minutesock.dawordgame.core.data.ValidWordDataSource
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.feature.game.GameSetupHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class GameSetupHelperTest {
    private val testDispatcher = StandardTestDispatcher()
    private val koin = startKoin {
        modules(testDbModule(testDispatcher))
    }.koin

    private val validWordDataSource: ValidWordDataSource = SqlDelightValidWordDataSource(koin.get())

    private val gameSetupHelper =
        GameSetupHelper(
            validWordDataSource = validWordDataSource,
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
        validWordDataSource.getValidWordCount()
        assertEquals(gameLanguage.expectedValidWordCount, validWordDataSource.getValidWordCount())
    }
}