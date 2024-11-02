package com.minutesock.dawordgame.feature.game.presentation.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.minutesock.dawordgame.core.theme.AppTheme
import com.minutesock.dawordgame.core.uiutil.rememberShakeController
import com.minutesock.dawordgame.core.uiutil.shake
import com.minutesock.dawordgame.feature.game.presentation.GameViewModelState
import com.minutesock.dawordgame.feature.game.presentation.WordGameEvent
import com.minutesock.dawordgame.feature.game.presentation.ui.component.FalseKeyboard
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PlayGameScreen(
    state: GameViewModelState,
    onEvent: (WordGameEvent) -> Unit
) {
    val shakeController = rememberShakeController()

    Column(
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

//        state.guessWordItems.forEach {
//            WordRow(
//                guessWordItem = it,
//                guessLetterItems = it.letters,
//                message = "", // state.dailyWordStateMessage?.uiText?.asString(),
//                wordRowAnimating = state.wordRowAnimating,
//                onEvent = onEvent
//            )
//        }

        FalseKeyboard(
            modifier = Modifier.fillMaxSize(),
            onEvent = onEvent,
            falseKeyboardKeys = state.falseKeyboardKeys,
            isWordRowAnimating = state.wordRowAnimating
        )
    }
}

@Preview
@Composable
private fun PlayGameScreenPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            PlayGameScreen(
                state = GameViewModelState(
//                    guessWordItems = persistentListOf(
//                        GuessWordItem(
//                            letters = persistentListOf(
//                                GuessLetterItem('c'),
//                                GuessLetterItem('h'),
//                                GuessLetterItem('u'),
//                                GuessLetterItem('m'),
//                                GuessLetterItem('b')
//                            )
//                        )
//                    )
                ),
                onEvent = {}
            )
        }
    }
}