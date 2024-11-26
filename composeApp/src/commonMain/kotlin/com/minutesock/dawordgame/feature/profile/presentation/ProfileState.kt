package com.minutesock.dawordgame.feature.profile.presentation

import com.minutesock.dawordgame.core.data.DataStoreManager

data class ProfileState(
    val darkModeToggle: Boolean = DataStoreManager.darkMode,
    val useSystemThemeToggle: Boolean = DataStoreManager.useSystemTheme
) {
    val darkModeToggleEnabled get() = !useSystemThemeToggle
}
