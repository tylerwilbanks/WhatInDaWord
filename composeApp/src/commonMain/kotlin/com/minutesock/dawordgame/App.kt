package com.minutesock.dawordgame

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.minutesock.dawordgame.feature.game.presentation.GameScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        GameScreen()
    }
}