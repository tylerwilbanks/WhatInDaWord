package com.minutesock.dawordgame.feature.game.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minutesock.dawordgame.di.KoinProvider
import com.minutesock.dawordgame.feature.game.data.GameRepository
import com.minutesock.dawordgame.feature.game.presentation.GameMode
import com.minutesock.dawordgame.feature.game.presentation.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = viewModel {
        GameViewModel(
            gameRepository = GameRepository(
                validWordDataSource = KoinProvider.instance.get(),
                wordSelectionDataSource = KoinProvider.instance.get(),
                defaultDispatcher = Dispatchers.IO
            )
        )
    }
) {
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        gameViewModel.setupGame(GameMode.Daily)
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(modifier = Modifier.wrapContentSize(), onClick = {}) {
            Text("This is some test text!")
        }
    }
}