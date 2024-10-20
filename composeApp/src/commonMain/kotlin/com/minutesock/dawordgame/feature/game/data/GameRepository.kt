package com.minutesock.dawordgame.feature.game.data

import com.minutesock.dawordgame.core.data.ValidWordDataSource
import com.minutesock.dawordgame.core.data.WordSelectionDataSource
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.WordSelection
import com.minutesock.dawordgame.feature.game.GameSetupHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.todayIn
import kotlin.random.Random

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

    suspend fun selectMysteryWordByDate(gameLanguage: GameLanguage,
                                        date: LocalDateTime = Clock.System.todayIn(TimeZone.currentSystemDefault())
                                            .atTime(0, 0)
    ): WordSelection {
        return withContext(defaultDispatcher) {
            val wordSelection = wordSelectionDataSource.selectAll(gameLanguage)
            wordSelection[Random(date.dayOfYear).nextInt(from = 0, until = wordSelection.size)]
        }
    }

    suspend fun selectMysteryWord(gameLanguage: GameLanguage): WordSelection {
        return withContext(defaultDispatcher) {
            wordSelectionDataSource.selectAll(gameLanguage).random()
        }
    }
}