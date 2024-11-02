package com.minutesock.dawordgame.feature.game.presentation

import com.minutesock.dawordgame.core.uiutil.TextRes
import whatindaword.composeapp.generated.resources.Res
import whatindaword.composeapp.generated.resources.there_are_no_words_to_edit
import whatindaword.composeapp.generated.resources.this_word_is_empty
import whatindaword.composeapp.generated.resources.this_word_is_full
import whatindaword.composeapp.generated.resources.unknown_error

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
                Unknown -> TextRes.StringRes(Res.string.unknown_error)
                NoWordToEdit -> TextRes.StringRes(Res.string.there_are_no_words_to_edit)
                NoLettersAvailableForInput -> TextRes.StringRes(Res.string.this_word_is_full)
                NoLettersToRemove -> TextRes.StringRes(Res.string.this_word_is_empty)
            }
        }
}