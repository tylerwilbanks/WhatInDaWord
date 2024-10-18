package com.minutesock.dawordgame.feature.game.data

import com.minutesock.dawordgame.core.data.ValidWordDataSource
import com.minutesock.dawordgame.core.data.WordSelectionDataSource
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.feature.game.GameSetupHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class GameRepository(
    private val validWordDataSource: ValidWordDataSource,
    private val wordSelectionDataSource: WordSelectionDataSource,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun setupWords(gameLanguage: GameLanguage) {
        withContext(defaultDispatcher) {
            GameSetupHelper(
                validWordDataSource = validWordDataSource,
                wordSelectionDataSource = wordSelectionDataSource,
                defaultDispatcher = defaultDispatcher
            ).apply {
                upsertValidWordsIfNeeded(gameLanguage)
                upsertWordSelectionIfNeeded(gameLanguage)
            }
        }
    }


    suspend fun testingCount(): Pair<Long, Long> {
        return withContext(defaultDispatcher) {
            Pair(validWordDataSource.getCount(), wordSelectionDataSource.getCount())
        }
    }
}