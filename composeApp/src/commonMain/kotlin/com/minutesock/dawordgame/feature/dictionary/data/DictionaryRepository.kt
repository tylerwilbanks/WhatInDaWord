package com.minutesock.dawordgame.feature.dictionary.data

import com.minutesock.dawordgame.core.data.WordSelectionDataSource
import com.minutesock.dawordgame.core.data.source.WordSessionDataSource
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.uiutil.TextRes
import com.minutesock.dawordgame.core.util.ContinuousOption
import com.minutesock.dawordgame.core.util.ContinuousStatus
import com.minutesock.dawordgame.core.util.GeneralIssue
import com.minutesock.dawordgame.di.KoinProvider
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryHeaderItem
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryWordEntryListItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

data class UnlockedWordEntryData(
    val language: GameLanguage = GameLanguage.English,
    val unlockedWordCount: Int = 0,
    val totalWordCount: Int = 0,
    val headerItems: ImmutableList<DictionaryHeaderItem> = persistentListOf()
)

class DictionaryRepository(
    private val wordSessionDataSource: WordSessionDataSource = KoinProvider.instance.get(),
    private val wordSelectionDataSource: WordSelectionDataSource = KoinProvider.instance.get(),
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getAlphabeticalUnlockedWordEntries(
        language: GameLanguage
    ) = flow<ContinuousOption<UnlockedWordEntryData, GeneralIssue>> {
        emit(
            ContinuousOption.Loading(
                data = UnlockedWordEntryData(language = language),
                continuousStatus = ContinuousStatus.Indefinite(textRes = TextRes.empty())
            )
        )

        val groupedMysteryWords = wordSessionDataSource.selectCompletedMysteryWordsSortedAlphabetically(
            language = language
        ).groupBy { it }
        val listItems = groupedMysteryWords.map {
            DictionaryWordEntryListItem(
                word = it.key,
                sessionCount = it.value.size
            )
        }
        val headers = listItems.groupBy { it.word.first() }
        val headerItems = headers.map {
            DictionaryHeaderItem(
                char = it.key.uppercaseChar(),
                listItems = it.value.toImmutableList()
            )
        }

        emit(
            ContinuousOption.Success(
                data = UnlockedWordEntryData(
                    language = language,
                    unlockedWordCount = headerItems.sumOf { it.listItems.size },
                    totalWordCount = wordSelectionDataSource.getCount(gameLanguage = language).toInt(),
                    headerItems = headerItems.toImmutableList()
                )
            )
        )
    }.flowOn(defaultDispatcher)
}