package com.minutesock.dawordgame.feature.profile.presentation.ui

import com.minutesock.dawordgame.feature.profile.presentation.ProfilePreference

data class ProfilePreferenceState(
    val preference: ProfilePreference,
    val value: Boolean,
    val enabled: Boolean
)
