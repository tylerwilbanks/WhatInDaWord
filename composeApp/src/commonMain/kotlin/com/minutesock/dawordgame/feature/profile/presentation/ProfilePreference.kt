package com.minutesock.dawordgame.feature.profile.presentation

import com.minutesock.dawordgame.core.uiutil.TextRes

// todo extract these
enum class ProfilePreference(val displayName: TextRes, val description: TextRes) {
    UseSystemTheme(
        displayName = TextRes.Raw("Use System Theme"),
        description = TextRes.Raw("Use system theme to match your device's dark mode settings."),
    ),
    DarkMode(
        displayName = TextRes.Raw("Dark Mode"),
        description = TextRes.Raw("Switch between dark and light mode."),
    ),
    ShowLogo(
        displayName = TextRes.Raw("Show Logo"),
        description = TextRes.Raw("Show the MinuteSock logo animation on app startup."),
    );
}