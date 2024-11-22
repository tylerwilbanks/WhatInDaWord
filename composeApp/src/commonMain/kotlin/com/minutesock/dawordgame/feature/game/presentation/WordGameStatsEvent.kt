package com.minutesock.dawordgame.feature.game.presentation

sealed class WordGameStatsEvent {
    data object PressExit : WordGameStatsEvent()
    data object PressShare : WordGameStatsEvent()
    data object DeleteAndRestartDailyGame : WordGameStatsEvent()
    data object NextInfinitySession : WordGameStatsEvent()
}