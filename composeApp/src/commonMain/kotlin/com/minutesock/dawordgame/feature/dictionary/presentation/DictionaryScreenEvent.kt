package com.minutesock.dawordgame.feature.dictionary.presentation

import androidx.navigation.NavController

sealed class DictionaryScreenEvent {
    data class WordEntryClick(val navController: NavController, val word: String) : DictionaryScreenEvent()
}