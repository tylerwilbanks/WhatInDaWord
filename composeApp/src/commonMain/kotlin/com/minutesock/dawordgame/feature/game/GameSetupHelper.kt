package com.minutesock.dawordgame.feature.game

//class GameSetupHelper(
//    private val appDatabase: AppDatabase, // todo-tyler replace with db client
//    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
//) {
//    suspend fun upsertValidWordsIfNeeded(gameLanguage: GameLanguage) {
//        withContext(defaultDispatcher) {
//            val expectedValidWordCount = gameLanguage.expectedValidWordCount
//            val validWordDao = appDatabase.getValidWordDao()
//            val storedValidWordCount = validWordDao.getValidWordCount()
//            if (storedValidWordCount != expectedValidWordCount) {
//                validWordDao.nukeTable()
//                val validWordsDto = Json.decodeFromString<ValidWordsDto>(readFile(gameLanguage.validWordFileName))
//                val validWords = validWordsDto.words.map { ValidWord(word = it, language = gameLanguage.dbName) }
//                validWordDao.upsert(validWords)
//            }
//        }
//    }
//
//    suspend fun upsertWordSelectionIfNeeded(gameLanguage: GameLanguage) {
//        withContext(defaultDispatcher) {
//            val expectedWordSelectionCount = gameLanguage.expectedWordSelectionCount
//            // todo-tyler
//        }
//    }
//}