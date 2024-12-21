package com.minutesock.dawordgame.core.uiutil

import androidx.compose.ui.graphics.Color

fun blendColors(color1: Color, color2: Color, ratio: Float): Color {
    val clampedRatio = ratio.coerceIn(0f, 1f)
    val inverseRatio = 1 - clampedRatio

    val red = (color1.red * inverseRatio + color2.red * clampedRatio)
    val green = (color1.green * inverseRatio + color2.green * clampedRatio)
    val blue = (color1.blue * inverseRatio + color2.blue * clampedRatio)
    val alpha = (color1.alpha * inverseRatio + color2.alpha * clampedRatio)

    return Color(red, green, blue, alpha)
}