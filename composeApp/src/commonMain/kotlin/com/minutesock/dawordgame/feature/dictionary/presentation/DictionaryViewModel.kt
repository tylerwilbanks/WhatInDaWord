package com.minutesock.dawordgame.feature.dictionary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.dawordgame.core.util.ContinuousOption
import com.minutesock.dawordgame.feature.dictionary.data.DictionaryRepository
import com.minutesock.dawordgame.getSystemLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DictionaryViewModel(
    private val dictionaryRepository: DictionaryRepository = DictionaryRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(DictionaryState())
    val state = _state.asStateFlow()

    fun updateListIfNeeded() {
        viewModelScope.launch {
            val oldCount = state.value.completedWordSessionsCount
            val existingCount = dictionaryRepository.getCompletedWordSessionsCount(state.value.language)
            if (oldCount != existingCount) {
                getUnlockedWordEntries()
            }
        }
    }

    private suspend fun getUnlockedWordEntries() {
        dictionaryRepository.getAlphabeticalUnlockedWordEntries(getSystemLanguage()).collect { continuousOption ->
            when (continuousOption) {
                is ContinuousOption.Issue -> Unit
                is ContinuousOption.Loading -> {
                    val data = continuousOption.data
                    _state.update {
                        it.copy(
                            loading = true,
                            language = data.language,
                            unlockedWordCount = data.unlockedWordCount,
                            totalWordCount = data.totalWordCount,
                            headerItems = data.headerItems
                        )
                    }
                }

                is ContinuousOption.Success -> {
                    val data = continuousOption.data
                    _state.update {
                        it.copy(
                            loading = false,
                            language = data.language,
                            unlockedWordCount = data.unlockedWordCount,
                            totalWordCount = data.totalWordCount,
                            headerItems = data.headerItems
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: DictionaryScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is DictionaryScreenEvent.WordEntryClick -> event.navController.navigate(event.args)
                is DictionaryScreenEvent.ScrollPosition -> {
                    _state.update { it.copy(scrollPosition = event.scrollPosition) }
                }
            }
        }
    }
}