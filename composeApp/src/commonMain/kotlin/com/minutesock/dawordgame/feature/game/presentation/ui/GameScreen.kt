package com.minutesock.dawordgame.feature.game.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.feature.game.data.GameRepository
import com.minutesock.dawordgame.feature.game.domain.GuessWordValidator
import com.minutesock.dawordgame.feature.game.presentation.GameViewModel

@Composable
fun GameScreen(
    navController: NavController,
    gameMode: GameMode,
    isDarkMode: Boolean,
    gameViewModel: GameViewModel = viewModel {
        GameViewModel(
            gameRepository = GameRepository(),
            guessWordValidator = GuessWordValidator()
        )
    },
    modifier: Modifier = Modifier
) {
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        gameViewModel.setupGame(gameMode)
    }

    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        PlayGameScreen(
            navController = navController,
            isDarkMode = isDarkMode,
            state = state,
            onEvent = gameViewModel::onEvent
        )
    }
}