package com.minutesock.dawordgame.feature.game

import com.minutesock.dawordgame.core.GameLanguage
import com.minutesock.dawordgame.database.AppDatabase
import com.minutesock.dawordgame.database.model.ValidWord
import com.minutesock.dawordgame.game.ValidWordsDto
import com.minutesock.dawordgame.readFile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class GameSetupHelper(
    private val appDatabase: AppDatabase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun upsertValidWordsIfNeeded(gameLanguage: GameLanguage) {
        withContext(defaultDispatcher) {
            val expectedValidWordCount = gameLanguage.expectedValidWordCount
            val validWordDao = appDatabase.getValidWordDao()
            val storedValidWordCount = validWordDao.getValidWordCount()
            if (storedValidWordCount != expectedValidWordCount) {
                validWordDao.nukeTable()
                val validWordsDto = Json.decodeFromString<ValidWordsDto>(readFile(gameLanguage.validWordFileName))
                val validWords = validWordsDto.words.map { ValidWord(word = it, language = gameLanguage.dbName) }
                validWordDao.upsert(validWords)
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