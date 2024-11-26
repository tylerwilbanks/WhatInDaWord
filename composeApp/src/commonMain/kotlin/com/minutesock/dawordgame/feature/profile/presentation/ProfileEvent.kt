package com.minutesock.dawordgame.feature.profile.presentation

sealed class ProfileEvent {
    data object DarkModeToggle : ProfileEvent()
    data object UseSystemThemeToggle : ProfileEvent()
    data object ClickWebsite : ProfileEvent()
}