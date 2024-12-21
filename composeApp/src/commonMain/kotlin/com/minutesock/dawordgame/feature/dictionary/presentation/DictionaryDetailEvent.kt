package com.minutesock.dawordgame.feature.dictionary.presentation

sealed class DictionaryDetailEvent {
    data object PressDictionaryDotCom : DictionaryDetailEvent()
}