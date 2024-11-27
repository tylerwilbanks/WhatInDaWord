import com.minutesock.dawordgame.core.data.DbClient
import com.minutesock.dawordgame.core.data.repository.GameRepository
import com.minutesock.dawordgame.core.data.source.WordEntryDataSource
import com.minutesock.dawordgame.core.data.toWordEntry
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.remote.WordHttpClient
import com.minutesock.dawordgame.core.remote.definition.DefinitionDto
import com.minutesock.dawordgame.core.remote.definition.MeaningDto
import com.minutesock.dawordgame.core.remote.definition.WordEntryDto
import com.minutesock.dawordgame.core.util.ContinuousOption
import com.minutesock.dawordgame.core.util.onSuccess
import com.minutesock.dawordgame.di.initKoinForTesting
import com.minutesock.dawordgame.di.testDbModule
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

class DefinitionFetchTest {
    private val testDispatcher = StandardTestDispatcher()

    private val koin = initKoinForTesting {
        modules(testDbModule(testDispatcher))
    }.koin

    private val dbClient: DbClient = koin.get()
    private val httpClient: HttpClient = koin.get()
    private val wordHttpClient: WordHttpClient = WordHttpClient(httpClient, testDispatcher)
    private val wordEntryDataSource: WordEntryDataSource = koin.get()

    private val mockWordEntryDto = WordEntryDto(
        word = "flame",
        meanings =
        listOf(
            MeaningDto(
                partOfSpeech = "noun",
                definitions = listOf(
                    DefinitionDto(
                        definition = "The visible part of fire; a stream of burning vapour or gas, emitting light and heat.",
                    ),
                    DefinitionDto(
                        definition = "A romantic partner or lover in a usually short-lived but passionate affair."
                    ),
                    DefinitionDto(
                        definition = "Intentionally insulting criticism or remark meant to incite anger."
                    ),
                    DefinitionDto(
                        definition = "A brilliant reddish orange-gold fiery colour."
                    ),
                    DefinitionDto(
                        definition = "The contrasting light and dark figure seen in wood used for stringed instrument making; the curl.",
                        example = "The cello has a two-piece back with a beautiful narrow flame."
                    ),
                    DefinitionDto(
                        definition = "Burning zeal, passion, imagination, excitement, or anger."
                    )
                )
            )
        ),
        phonetics = emptyList()
    )

    @AfterTest
    fun teardown() = runTest(testDispatcher) {
        dbClient.clearDb()
        stopKoin()
    }

    @Test
    fun en_definitionWebsiteIsStillUp() = runTest(testDispatcher) {
        val url = "${WordHttpClient.baseUrl}api/v2/entries/en/dog"
        val response = httpClient.get(url)
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun en_definitionDTOMapping() = runTest(testDispatcher) {
        wordHttpClient.fetchWordDefinition(GameLanguage.English, "flame").onSuccess { entries ->
            assertTrue { entries.isNotEmpty() }
            val examineEntry = entries.first()
            assertEquals("flame", examineEntry.word)
            assertEquals(
                "The visible part of fire; a stream of burning vapour or gas, emitting light and heat.",
                examineEntry.meanings.first().definitions.first().definition
            )
        }
    }

    @Test
    fun en_wordEntryAndWordDefinitionsInsertion() = runTest(testDispatcher) {
        val language = GameLanguage.English
        wordEntryDataSource.upsertEntriesAndDefinitions(
            mockWordEntryDto.toWordEntry(language)
        )
        val wordEntry = wordEntryDataSource.selectByWord(language, mockWordEntryDto.word)
        assertNotNull(wordEntry)
        assertEquals(mockWordEntryDto.word, wordEntry.word)
        assertEquals(Clock.System.todayIn(TimeZone.currentSystemDefault()), wordEntry.fetchDate)
        assertEquals(null, wordEntry.origin)
        assertEquals(mockWordEntryDto.phonetic, wordEntry.phonetic)
        assertTrue(wordEntry.definitions.isNotEmpty())
        for (i in 0..5) {
            wordEntry.definitions[i].apply {
                val mockMeaning = mockWordEntryDto.meanings.first()
                val mockDefinition = mockMeaning.definitions[i]
                assertEquals(mockDefinition.definition, definition)
                assertEquals(mockDefinition.example, example)
                assertEquals(wordEntry.word, word)
                assertEquals(mockMeaning.partOfSpeech, partOfSpeech)
                assertEquals(language, this.language)
            }
        }
    }

    @Test
    fun en_shouldFetchEmptyDatabase() = runTest(testDispatcher) {
        val testWord = "flame"
        val language = GameLanguage.English
        assertEquals(0, wordEntryDataSource.selectCountByWord(language, testWord))
        val gameRepository = GameRepository(
            wordHttpClient = wordHttpClient
        )

        val results = gameRepository.getOrFetchWordEntry(
            language = language,
            word = testWord,
            throttle = null
        ).toList()

        assertEquals(4, results.size)

        when (val lastResult = results.last()) {
            is ContinuousOption.Issue -> fail("last result was an issue: ${lastResult.issue.textRes.asRawString()}")
            is ContinuousOption.Loading -> fail("last result was ${lastResult::class.simpleName}")
            is ContinuousOption.Success -> {
                assertEquals(1, wordEntryDataSource.selectCountByWord(language, testWord))
                val data = lastResult.data
                assertNotNull(data)
                assertEquals(testWord, data.word)
                assertEquals(Clock.System.todayIn(TimeZone.currentSystemDefault()), data.fetchDate)
                assertTrue(data.definitions.isNotEmpty())
                assertTrue(data.definitions.first().definition.isNotBlank())
            }
        }
    }

    @Test
    fun en_noFetchUseExistingWordEntry() = runTest(testDispatcher) {
        val firstDefinition = "sneeze"
        val language = GameLanguage.English
        val mockWordEntry = mockWordEntryDto.copy(
            meanings = mockWordEntryDto.meanings.toMutableList().apply {
                this[0] = this[0].copy(
                    definitions = this[0].definitions.toMutableList().apply {
                        this[0] = this[0].copy(definition = firstDefinition)
                    }
                )
            }
        ).toWordEntry(language)
        val testWord = mockWordEntry.word

        assertNull(wordEntryDataSource.selectByWord(language, testWord))
        wordEntryDataSource.upsertEntriesAndDefinitions(mockWordEntry)
        assertEquals(1, wordEntryDataSource.selectCountByWord(language, testWord))

        val gameRepository = GameRepository(
            wordHttpClient = wordHttpClient,
            defaultDispatcher = testDispatcher
        )

        val results = gameRepository.getOrFetchWordEntry(
            language = language,
            word = testWord,
        ).toList()

        // the 3 here means that 3 ContinuousOptions were emitted, meaning that the fetch block was skipped.
        assertEquals(3, results.size)
        assertTrue { results.last() is ContinuousOption.Success }
        results.last().onSuccess {
            val data = it
            assertNotNull(data)
            assertEquals(firstDefinition, data.definitions.first().definition)
        }
    }

    @Test
    fun en_shouldFetchWordEntryPassThrottleCheck() = runTest(testDispatcher) {
        val firstDefinition = "sneeze"
        val language = GameLanguage.English
        val mockWordEntry = mockWordEntryDto.copy(
            meanings = mockWordEntryDto.meanings.toMutableList().apply {
                this[0] = this[0].copy(
                    definitions = this[0].definitions.toMutableList().apply {
                        this[0] = this[0].copy(definition = firstDefinition)
                    }
                )
            }
        ).toWordEntry(
            language = language,
            fetchDate = Clock.System.todayIn(TimeZone.currentSystemDefault()).minus(DatePeriod(days = 15))
        )
        val testWord = mockWordEntry.word

        assertNull(wordEntryDataSource.selectByWord(language, testWord))
        wordEntryDataSource.upsertEntriesAndDefinitions(mockWordEntry)
        assertEquals(1, wordEntryDataSource.selectCountByWord(language, testWord))

        val gameRepository = GameRepository(
            wordHttpClient = wordHttpClient,
            defaultDispatcher = testDispatcher
        )

        val results = gameRepository.getOrFetchWordEntry(
            language = language,
            word = testWord,
        ).toList()

        // the 4 here means there were 4 ContinuousOptions emitted, meaning that it hit the fetch block
        assertEquals(4, results.size)
        assertTrue { results.last() is ContinuousOption.Success }
        results.last().onSuccess {
            val data = it
            assertNotNull(data)
            assertNotEquals(firstDefinition, data.definitions.first().definition)
        }
    }
}