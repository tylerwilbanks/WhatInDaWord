import com.minutesock.dawordgame.game.ValidWordsDto
import com.minutesock.dawordgame.game.WordSelectionDto
import com.minutesock.dawordgame.readFile
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonParseTest {

    @Test
    fun readValidWordsFile() {
        val text = readFile("valid_words.json")
        assertTrue(text.isNotEmpty())
    }

    @Test
    fun readWordSelectionFile() {
        val text = readFile("word_selection.json")
        assertTrue(text.isNotEmpty())
    }

    @Test
    fun parseValidWords() {
        val text = readFile("valid_words.json")
        val validWordsDto = Json.decodeFromString<ValidWordsDto>(text)
        assertEquals(12_484, validWordsDto.words.size)
    }

    @Test
    fun parseWordSelection() {
        val text = readFile("word_selection.json")
        val wordSelectionDto = Json.decodeFromString<WordSelectionDto>(text)
        assertEquals(2_315, wordSelectionDto.words.size)
    }
}