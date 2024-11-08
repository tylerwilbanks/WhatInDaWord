package com.minutesock.dawordgame.core.theme

import androidx.compose.ui.graphics.Color

val md_theme_light_primary = Color(0xFF006D38)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFF71FDA2)
val md_theme_light_onPrimaryContainer = Color(0xFF00210D)
val md_theme_light_secondary = Color(0xFF506353)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFD2E8D3)
val md_theme_light_onSecondaryContainer = Color(0xFF0D1F12)
val md_theme_light_tertiary = Color(0xFF3A656F)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFBEEAF6)
val md_theme_light_onTertiaryContainer = Color(0xFF001F25)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFBFDF7)
val md_theme_light_onBackground = Color(0xFF191C19)
val md_theme_light_surface = Color(0xFFFBFDF7)
val md_theme_light_onSurface = Color(0xFF191C19)
val md_theme_light_surfaceVariant = Color(0xFFDDE5DB)
val md_theme_light_onSurfaceVariant = Color(0xFF414941)
val md_theme_light_outline = Color(0xFF717971)
val md_theme_light_inverseOnSurface = Color(0xFFF0F1EC)
val md_theme_light_inverseSurface = Color(0xFF2E312E)
val md_theme_light_inversePrimary = Color(0xFF51E088)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF006D38)
val md_theme_light_outlineVariant = Color(0xFFC1C9BF)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFF51E088)
val md_theme_dark_onPrimary = Color(0xFF00391B)
val md_theme_dark_primaryContainer = Color(0xFF005229)
val md_theme_dark_onPrimaryContainer = Color(0xFF71FDA2)
val md_theme_dark_secondary = Color(0xFFB6CCB8)
val md_theme_dark_onSecondary = Color(0xFF223526)
val md_theme_dark_secondaryContainer = Color(0xFF384B3C)
val md_theme_dark_onSecondaryContainer = Color(0xFFD2E8D3)
val md_theme_dark_tertiary = Color(0xFFA2CED9)
val md_theme_dark_onTertiary = Color(0xFF01363F)
val md_theme_dark_tertiaryContainer = Color(0xFF204D56)
val md_theme_dark_onTertiaryContainer = Color(0xFFBEEAF6)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF191C19)
val md_theme_dark_onBackground = Color(0xFFE1E3DE)
val md_theme_dark_surface = Color(0xFF191C19)
val md_theme_dark_onSurface = Color(0xFFE1E3DE)
val md_theme_dark_surfaceVariant = Color(0xFF414941)
val md_theme_dark_onSurfaceVariant = Color(0xFFC1C9BF)
val md_theme_dark_outline = Color(0xFF8B938A)
val md_theme_dark_inverseOnSurface = Color(0xFF191C19)
val md_theme_dark_inverseSurface = Color(0xFFE1E3DE)
val md_theme_dark_inversePrimary = Color(0xFF006D38)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFF51E088)
val md_theme_dark_outlineVariant = Color(0xFF414941)
val md_theme_dark_scrim = Color(0xFF000000)

val guessLetterYellow = Color(172, 169, 34, 255) // #ACA922
val guessLetterGreen = Color(0xFF189818) // #189818
val guessLetterAbsent = Color(0xFF8c8c8c)

val seed = Color(0xFF00B260)

fun Color.brighten(factor: Float): Color {
    val clampedFactor = factor.coerceIn(0f, 1f)
    val newRed = red + (1.0f - red) * clampedFactor
    val newGreen = green + (1.0f - green) * clampedFactor
    val newBlue = blue + (1.0f - blue) * clampedFactor
    return Color(newRed, newGreen, newBlue, alpha)
}
