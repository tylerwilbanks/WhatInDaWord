import com.minutesock.dawordgame.core.data.DbClient
import com.minutesock.dawordgame.core.data.source.WordEntryDataSource
import com.minutesock.dawordgame.core.data.toWordEntry
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.remote.createHttpClient
import com.minutesock.dawordgame.core.remote.createHttpClientEngine
import com.minutesock.dawordgame.core.remote.definition.DefinitionDto
import com.minutesock.dawordgame.core.remote.definition.MeaningDto
import com.minutesock.dawordgame.core.remote.definition.WordEntryDto
import com.minutesock.dawordgame.core.util.onSuccess
import com.minutesock.dawordgame.di.initKoinForTesting
import com.minutesock.dawordgame.di.testDbModule
import com.minutesock.dawordgame.feature.game.remote.WordHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DefinitionFetchTest {
    private val testDispatcher = StandardTestDispatcher()

    private val koin = initKoinForTesting {
        modules(
            testDbModule(testDispatcher),
            module {
                single { createHttpClient(createHttpClientEngine()) }
            }
        )
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
}