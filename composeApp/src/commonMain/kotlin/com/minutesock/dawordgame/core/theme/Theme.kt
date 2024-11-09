package com.minutesock.dawordgame.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.minutesock.dawordgame.core.data.DataStoreManager
import com.minutesock.dawordgame.core.data.rememberPreference

private val lightColors = lightColors(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
)

private val darkColors = darkColors(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
)

@Composable
fun rememberDarkTheme(): State<Boolean> {
    val darkTheme by rememberPreference(DataStoreManager.darkModeDelegate)
    val useSystemTheme by rememberPreference(DataStoreManager.useSystemThemeDelegate)
    val isSystemInDarkTheme = isSystemInDarkTheme()

    return remember(darkTheme, useSystemTheme, isSystemInDarkTheme) {
        derivedStateOf {
            if (useSystemTheme) {
                isSystemInDarkTheme
            } else {
                darkTheme
            }
        }
    }
}

@Composable
fun AppTheme(
    content: @Composable() () -> Unit
) {
    val darkTheme by rememberPreference(DataStoreManager.darkModeDelegate)
    val useSystemTheme by rememberPreference(DataStoreManager.useSystemThemeDelegate)

    val darkMode = if (useSystemTheme) {
        isSystemInDarkTheme()
    } else {
        darkTheme
    }

    MaterialTheme(
        colors = if (darkMode) {
            darkColors
        } else {
            lightColors
        },
        content = content
    )
}