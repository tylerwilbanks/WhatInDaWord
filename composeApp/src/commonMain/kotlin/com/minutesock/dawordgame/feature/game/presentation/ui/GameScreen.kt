package com.minutesock.dawordgame.feature.game.presentation.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minutesock.dawordgame.core.uiutil.rememberShakeController
import com.minutesock.dawordgame.core.uiutil.shake
import com.minutesock.dawordgame.di.KoinProvider
import com.minutesock.dawordgame.feature.game.data.GameRepository
import com.minutesock.dawordgame.feature.game.presentation.GameMode
import com.minutesock.dawordgame.feature.game.presentation.GameViewModel
import com.minutesock.dawordgame.feature.game.presentation.ui.component.WordRow
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
    val shakeController = rememberShakeController()
    LaunchedEffect(Unit) {
        gameViewModel.setupGame(GameMode.Daily)
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .shake(shakeController)
                    .padding(bottom = 15.dp, start = 10.dp, end = 10.dp)
                    .animateContentSize(),

                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3,
                text = "" // state.dailyWordStateMessage?.textRes?.asString() ?: "",
                // color = messageColor
            )
        }

        state.guessWords.forEach {
            WordRow(
                guessWord = it,
                guessLetters = it.letters,
                message = "", // state.dailyWordStateMessage?.uiText?.asString(),
                wordRowAnimating = state.wordRowAnimating,
                onEvent = gameViewModel::onEvent
            )
        }
    }
}