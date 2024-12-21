package com.minutesock.dawordgame.feature.game.presentation

import com.minutesock.dawordgame.core.domain.definition.WordEntry
import com.minutesock.dawordgame.core.util.ContinuousOption
import com.minutesock.dawordgame.core.util.ContinuousStatus
import com.minutesock.dawordgame.core.util.GeneralIssue

data class GameStatsState(
    val fetchState: ContinuousOption<WordEntry?, GeneralIssue> = ContinuousOption.Loading(
        data = null,
        continuousStatus = ContinuousStatus.Indefinite.empty()
    ),
)
