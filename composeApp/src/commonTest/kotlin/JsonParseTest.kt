import com.minutesock.dawordgame.core.domain.ValidWordsDto
import com.minutesock.dawordgame.core.domain.WordSelectionDto
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import whatindaword.composeapp.generated.resources.Res
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@OptIn(ExperimentalResourceApi::class)
class JsonParseTest {
    private val defaultDispatcher = StandardTestDispatcher()

    @Test
    fun readValidWordsFile() = runTest(defaultDispatcher) {
        val text = Res.readBytes("files/valid_words.json").decodeToString()
        assertTrue(text.isNotEmpty())
    }

    @Test
    fun readWordSelectionFile() = runTest(defaultDispatcher) {
        val text = Res.readBytes("files/word_selection.json").decodeToString()
        assertTrue(text.isNotEmpty())
    }

    @Test
    fun parseValidWords() = runTest(defaultDispatcher) {
        val text = Res.readBytes("files/valid_words.json").decodeToString()
        val validWordsDto = Json.decodeFromString<ValidWordsDto>(text)
        assertEquals(12_484, validWordsDto.words.size)
    }

    @Test
    fun parseWordSelection() = runTest(defaultDispatcher) {
        val text = Res.readBytes("files/word_selection.json").decodeToString()
        val wordSelectionDto = Json.decodeFromString<WordSelectionDto>(text)
        assertEquals(2_315, wordSelectionDto.words.size)
    }
}