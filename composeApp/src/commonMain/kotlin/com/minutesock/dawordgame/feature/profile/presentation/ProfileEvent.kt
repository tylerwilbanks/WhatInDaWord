package com.minutesock.dawordgame.feature.profile.presentation

sealed class ProfileEvent {
    data class PreferenceToggle(val preference: ProfilePreference) : ProfileEvent()
    data object ClickWebsite : ProfileEvent()
}