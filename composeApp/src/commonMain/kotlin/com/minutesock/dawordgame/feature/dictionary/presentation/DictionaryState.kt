package com.minutesock.dawordgame.feature.dictionary.presentation

import com.minutesock.dawordgame.core.domain.GameLanguage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DictionaryState(
    val loading: Boolean = false,
    val language: GameLanguage = GameLanguage.English,
    val unlockedWordCount: Int = 0,
    val totalWordCount: Int = 0,
    val headerItems: ImmutableList<DictionaryHeaderItem> = persistentListOf()
)

data class DictionaryHeaderItem(
    val char: Char,
    val listItems: ImmutableList<DictionaryWordEntryListItem> = persistentListOf()
)

data class DictionaryWordEntryListItem(
    val word: String,
    val sessionCount: Int
)