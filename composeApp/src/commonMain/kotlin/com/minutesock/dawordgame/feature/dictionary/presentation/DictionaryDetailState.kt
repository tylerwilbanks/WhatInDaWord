package com.minutesock.dawordgame.feature.dictionary.presentation

import com.minutesock.dawordgame.core.domain.definition.WordEntry
import com.minutesock.dawordgame.core.util.ContinuousOption
import com.minutesock.dawordgame.core.util.ContinuousStatus
import com.minutesock.dawordgame.core.util.GeneralIssue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DictionaryDetailState(
    val fetchState: ContinuousOption<WordEntry?, GeneralIssue> = ContinuousOption.Loading(
        data = null,
        continuousStatus = ContinuousStatus.Indefinite.empty()
    ),
    val sessions: ImmutableList<DictionaryWordSessionInfo> = persistentListOf()
)