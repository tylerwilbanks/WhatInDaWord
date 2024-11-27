package com.minutesock.dawordgame.feature.game.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.minutesock.dawordgame.core.data.repository.GameRepository
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.feature.game.domain.GuessWordValidator
import com.minutesock.dawordgame.feature.game.presentation.GameScreenState
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
    val gameState by gameViewModel.state.collectAsStateWithLifecycle()
    val statsState by gameViewModel.statsState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        gameViewModel.setupGame(gameMode)
    }

    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = gameState.screenState == GameScreenState.Game
        ) {
            PlayGameScreen(
                navController = navController,
                isDarkMode = isDarkMode,
                state = gameState,
                onEvent = gameViewModel::onEvent
            )
        }

        AnimatedVisibility(
            visible = gameState.screenState == GameScreenState.Stats,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            PlayGameStatsScreen(
                gameState = gameState,
                statsState = statsState,
                onEvent = gameViewModel::onStatsEvent,
            )
        }
    }
}