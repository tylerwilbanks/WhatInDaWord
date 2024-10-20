package com.minutesock.dawordgame.feature.game.presentation

import com.minutesock.dawordgame.core.uiutil.TextRes

enum class GuessWordError {
    None,
    Unknown,
    NoWordToEdit,
    NoLettersAvailableForInput,
    NoLettersToRemove;

    val message: TextRes
        get() {
            return when (this) {
                None -> TextRes.Raw(value = "")
                Unknown -> TODO() // UiText.StringResource(R.string.unknown_error)
                NoWordToEdit -> TODO() // UiText.StringResource(R.string.there_are_no_words_to_edit)
                NoLettersAvailableForInput -> TODO() // UiText.StringResource(R.string.this_word_is_full)
                NoLettersToRemove -> TODO() // UiText.StringResource(R.string.this_word_is_empty)
            }
        }
}