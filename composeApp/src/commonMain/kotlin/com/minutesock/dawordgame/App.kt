package com.minutesock.dawordgame

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minutesock.dawordgame.di.KoinProvider
import com.minutesock.dawordgame.feature.game.data.GameRepository
import com.minutesock.dawordgame.feature.game.presentation.GameViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    gameViewModel: GameViewModel = viewModel {
        GameViewModel(
            gameRepository = GameRepository(
                validWordDataSource = KoinProvider.instance.get(),
                wordSelectionDataSource = KoinProvider.instance.get(),
            )
        )
    }
) {

    val state by gameViewModel.state.collectAsStateWithLifecycle()
    MaterialTheme {
        Text("valid word count: ${state.validWordCount} | word selection count: ${state.wordSelectionCount}")
    }
}