import androidx.room.Room
import androidx.room.RoomDatabase
import com.minutesock.dawordgame.core.GameLanguage
import com.minutesock.dawordgame.database.AppDatabase
import com.minutesock.dawordgame.di.testModule
import com.minutesock.dawordgame.feature.game.GameSetupHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test




class GameSetupHelperTest {
    private val koin = startKoin {
        modules(testModule)
    }.koin

    private val db: AppDatabase = koin.get()
    private val gameSetupHelper: GameSetupHelper = koin.get()
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun teardown() {
        db.close()
        stopKoin()
    }

    @Test
    fun english_validWordsAreInsertedIntoDb() = runTest(testDispatcher) {
        val gameLanguage = GameLanguage.English
        val validWordDao = db.getValidWordDao()
        gameSetupHelper.upsertValidWordsIfNeeded(gameLanguage)
    }
}