package com.minutesock.dawordgame.feature.profile.presentation

import com.minutesock.dawordgame.feature.profile.presentation.ui.ProfilePreferenceState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ProfileState(
    val preferences: ImmutableList<ProfilePreferenceState> = persistentListOf(),
    val guessDistributionState: GuessDistributionState = GuessDistributionState()
)