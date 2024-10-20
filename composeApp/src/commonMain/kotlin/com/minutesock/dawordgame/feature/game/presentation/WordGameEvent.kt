package com.minutesock.dawordgame.feature.game.presentation

sealed class WordGameEvent {
    data class OnCharacterPress(val character: Char) : WordGameEvent()
    data object OnEnterPress : WordGameEvent()
    data object OnDeletePress : WordGameEvent()
    data object OnErrorAnimationFinished : WordGameEvent()
    data object OnAnsweredWordRowAnimationFinished : WordGameEvent()
    data object OnCompleteAnimationFinished : WordGameEvent()
    data object OnStatsPress : WordGameEvent()
}