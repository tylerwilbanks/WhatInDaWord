package com.minutesock.dawordgame.feature.dictionary.presentation

import androidx.navigation.NavController
import com.minutesock.dawordgame.core.navigation.NavigationDestination

sealed class DictionaryScreenEvent {
    data class WordEntryClick(
        val navController: NavController,
        val args: NavigationDestination.DictionaryDetail
    ) : DictionaryScreenEvent()

    data class ScrollPosition(val scrollPosition: Int) : DictionaryScreenEvent()
}