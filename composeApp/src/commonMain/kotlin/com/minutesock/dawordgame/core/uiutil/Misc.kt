package com.minutesock.dawordgame.core.uiutil

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun blendColors(color1: Color, color2: Color, ratio: Float): Color {
    require(ratio in 0.0f..1.0f) { "Ratio must be between 0.0 and 1.0" }

    val c1 = color1.toArgb()
    val c2 = color2.toArgb()
    val inverseRatio = 1.0f - ratio

    // Extract ARGB components manually
    val a1 = (c1 / (256 * 256 * 256)) / 255.0f
    val r1 = (c1 / (256 * 256) % 256) / 255.0f
    val g1 = (c1 / 256 % 256) / 255.0f
    val b1 = (c1 % 256) / 255.0f

    val a2 = (c2 / (256 * 256 * 256)) / 255.0f
    val r2 = (c2 / (256 * 256) % 256) / 255.0f
    val g2 = (c2 / 256 % 256) / 255.0f
    val b2 = (c2 % 256) / 255.0f

    // Blend the components
    val a = ((a1 * ratio) + (a2 * inverseRatio)).coerceIn(0.0f, 1.0f)
    val r = ((r1 * ratio) + (r2 * inverseRatio)).coerceIn(0.0f, 1.0f)
    val g = ((g1 * ratio) + (g2 * inverseRatio)).coerceIn(0.0f, 1.0f)
    val b = ((b1 * ratio) + (b2 * inverseRatio)).coerceIn(0.0f, 1.0f)

    // Recombine the components into a single ARGB value
    val blendedColorInt = ((a * 255).toUInt().toInt() shl 24 or
            (r * 255).toUInt().toInt() shl 16 or
            (g * 255).toUInt().toInt() shl 8 or
            (b * 255).toUInt().toInt())
    return Color(blendedColorInt)
}