package com.minutesock.dawordgame.feature.dictionary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.dawordgame.core.data.repository.GameRepository
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.WordSession
import com.minutesock.dawordgame.feature.dictionary.data.DictionaryRepository
import com.minutesock.dawordgame.openWebsite
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DictionaryDetailViewModel(
    private val word: String,
    private val language: GameLanguage,
    private val gameRepository: GameRepository = GameRepository(),
    private val dictionaryRepository: DictionaryRepository = DictionaryRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(DictionaryDetailState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            gameRepository.getOrFetchWordEntry(
                language = language,
                word = word
            ).collect { continuousOption ->
                _state.update {
                    it.copy(
                        fetchState = continuousOption
                    )
                }
            }
        }

        viewModelScope.launch {
            val sessions = dictionaryRepository
                .getWordSessions(language, word)
                .map(WordSession::toDictionaryWordSessionInfo)
                .toImmutableList()

            _state.update {
                it.copy(
                    sessions = sessions
                )
            }
        }
    }

    fun onEvent(event: DictionaryDetailEvent) {
        viewModelScope.launch {
            when (event) {
                DictionaryDetailEvent.PressDictionaryDotCom -> openWebsite("https://www.dictionary.com/browse/$word")
            }
        }
    }
}