package com.minutesock.dawordgame.feature.game

import com.minutesock.dawordgame.core.data.ValidWordDataSource
import com.minutesock.dawordgame.core.data.WordSelectionDataSource
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.ValidWord
import com.minutesock.dawordgame.core.domain.ValidWordsDto
import com.minutesock.dawordgame.core.domain.WordSelection
import com.minutesock.dawordgame.core.domain.WordSelectionDto
import com.minutesock.dawordgame.readFile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class GameSetupHelper(
    private val validWordDataSource: ValidWordDataSource,
    private val wordSelectionDataSource: WordSelectionDataSource,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun upsertValidWordsIfNeeded(gameLanguage: GameLanguage) {
        withContext(defaultDispatcher) {
            val expectedValidWordCount = gameLanguage.expectedValidWordCount
            val storedValidWordCount = validWordDataSource.getCount(gameLanguage)
            if (storedValidWordCount != expectedValidWordCount) {
                validWordDataSource.clearTable()
                val validWords = Json
                    .decodeFromString<ValidWordsDto>(readFile(gameLanguage.validWordFileName))
                    .words
                    .map { ValidWord(word = it, language = gameLanguage) }
                validWordDataSource.upsert(validWords)
            }
        }
    }

    suspend fun upsertWordSelectionIfNeeded(gameLanguage: GameLanguage) {
        withContext(defaultDispatcher) {
            val expectedWordSelectionCount = gameLanguage.expectedWordSelectionCount
            val storedWordSelectionCount = wordSelectionDataSource.getCount(gameLanguage)
            if (storedWordSelectionCount != expectedWordSelectionCount) {
                wordSelectionDataSource.clearTable()
                val wordSelections = Json
                    .decodeFromString<WordSelectionDto>(readFile(gameLanguage.wordSelectionFileName))
                    .words
                    .map { WordSelection(word = it, language = gameLanguage) }
                    .toTypedArray()
                wordSelectionDataSource.upsert(*wordSelections)
            }
        }
    }
}