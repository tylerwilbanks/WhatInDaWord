package com.minutesock.dawordgame.feature.profile.presentation

import com.minutesock.dawordgame.core.domain.GuessDistributionItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class GuessDistributionState(
    val loading: Boolean = false,
    val guessDistributions: ImmutableList<GuessDistributionItem> = persistentListOf(),
    val failedGameSessionsCount: Long = 0,
    val totalSessionCount: Long = 0
)