package com.minutesock.dawordgame.feature.game

import com.minutesock.dawordgame.core.data.ValidWordDataSource
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.ValidWord
import com.minutesock.dawordgame.game.ValidWordsDto
import com.minutesock.dawordgame.readFile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class GameSetupHelper(
    private val validWordDataSource: ValidWordDataSource,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun upsertValidWordsIfNeeded(gameLanguage: GameLanguage) {
        withContext(defaultDispatcher) {
            val expectedValidWordCount = gameLanguage.expectedValidWordCount
            val storedValidWordCount = validWordDataSource.getValidWordCount()
            if (storedValidWordCount != expectedValidWordCount) {
                validWordDataSource.clearValidWordEntities()
                val validWords = Json
                    .decodeFromString<ValidWordsDto>(readFile(gameLanguage.validWordFileName))
                    .words
                    .map { ValidWord(word = it, language = gameLanguage) }
                validWordDataSource.upsertValidWords(validWords)
            }
        }
    }

    suspend fun upsertWordSelectionIfNeeded(gameLanguage: GameLanguage) {
        withContext(defaultDispatcher) {
            val expectedWordSelectionCount = gameLanguage.expectedWordSelectionCount
            // todo-tyler
        }
    }
}