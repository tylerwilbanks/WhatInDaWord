package com.minutesock.dawordgame

import androidx.compose.runtime.Composable
import com.minutesock.dawordgame.core.theme.AppTheme
import com.minutesock.dawordgame.feature.game.presentation.ui.GameScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme { isDarkMode: Boolean ->
        GameScreen(
            isDarkMode = isDarkMode
        )
    }
}