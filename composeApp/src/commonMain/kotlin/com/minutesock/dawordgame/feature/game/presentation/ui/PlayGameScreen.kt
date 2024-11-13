package com.minutesock.dawordgame.feature.game.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.minutesock.dawordgame.core.domain.WordSessionState
import com.minutesock.dawordgame.core.navigation.NavigationDestination
import com.minutesock.dawordgame.core.theme.AppTheme
import com.minutesock.dawordgame.core.uiutil.ShakeConfig
import com.minutesock.dawordgame.core.uiutil.rememberShakeController
import com.minutesock.dawordgame.core.uiutil.shake
import com.minutesock.dawordgame.feature.game.presentation.GameLoadingState
import com.minutesock.dawordgame.feature.game.presentation.GameViewModelState
import com.minutesock.dawordgame.feature.game.presentation.WordGameEvent
import com.minutesock.dawordgame.feature.game.presentation.ui.component.FalseKeyboard
import com.minutesock.dawordgame.feature.game.presentation.ui.component.WordRow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import whatindaword.composeapp.generated.resources.Res
import whatindaword.composeapp.generated.resources.bar_chart
import whatindaword.composeapp.generated.resources.dark_mode
import whatindaword.composeapp.generated.resources.light_mode
import whatindaword.composeapp.generated.resources.question_mark
import whatindaword.composeapp.generated.resources.what_in_da_word

@Composable
fun PlayGameScreen(
    navController: NavController,
    isDarkMode: Boolean,
    state: GameViewModelState,
    onEvent: (WordGameEvent) -> Unit
) {
    val themeIconId by remember(isDarkMode) {
        mutableStateOf(
            when (isDarkMode) {
                false -> Res.drawable.light_mode
                true -> Res.drawable.dark_mode
            }
        )
    }

    val defaultMessageDelay by remember {
        mutableStateOf(1000L)
    }
    val shakeController = rememberShakeController()
    val messageColor by animateColorAsState(
        targetValue = if (
            state.gameTitleMessage.message.asString() != stringResource(Res.string.what_in_da_word) &&
            (state.gameTitleMessage.isError || state.gameState == WordSessionState.Failure)
        ) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(
            durationMillis = if (state.gameTitleMessage.isError) 200 else 1000,
            easing = LinearEasing
        ), label = "title message color"
    )

    LaunchedEffect(state.gameTitleMessage) {
        if (state.wordRowAnimating) {
            return@LaunchedEffect
        }
        if (state.gameTitleMessage.isError || state.gameState == WordSessionState.Failure) {
            shakeController.shake(
                ShakeConfig.no(defaultMessageDelay) {
                    if (state.gameState == WordSessionState.Failure) {
                        onEvent(WordGameEvent.OnCompleteAnimationFinished)
                    } else {
                        onEvent(WordGameEvent.OnErrorAnimationFinished)
                    }
                }
            )
            return@LaunchedEffect
        }
        if (state.gameState == WordSessionState.Success) {
            shakeController.shake(
                ShakeConfig.yes(defaultMessageDelay) {
                    onEvent(
                        WordGameEvent.OnCompleteAnimationFinished
                    )
                }
            )
            return@LaunchedEffect
        }

        shakeController.shake(
            ShakeConfig(
                iterations = 1,
                intensity = 1_000f,
                rotateX = 5f,
                translateY = 15f,
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.navigate(NavigationDestination.HowToPlay) }) {
                Icon(
                    modifier = Modifier
                        .size(45.dp)
                        .padding(10.dp),
                    painter = painterResource(Res.drawable.question_mark),
                    contentDescription = "How to play"
                )
            }

            IconButton(
                onClick = { onEvent(WordGameEvent.OnDarkThemeToggle) }
            ) {
                Icon(
                    modifier = Modifier
                        .size(45.dp)
                        .padding(10.dp),
                    painter = painterResource(resource = themeIconId),
                    contentDescription = "Toggle dark theme"
                )
            }
            IconButton(onClick = { onEvent(WordGameEvent.OnStatsPress) }) {
                Icon(
                    painterResource(resource = Res.drawable.bar_chart),
                    contentDescription = "Stats"
                )
            }
        }

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
                style = MaterialTheme.typography.headlineLarge,
                fontSize = TextUnit(28f, TextUnitType.Sp),
                text = state.gameTitleMessage.message.asString(),
                color = messageColor
            )
        }

        val lazyListState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.weight(1f),
            state = lazyListState
        ) {
            item {
                state.wordSession?.guesses?.forEach {
                    WordRow(
                        isDarkMode = isDarkMode,
                        guessWord = it,
                        guessLetters = it.letters,
                        message = state.gameTitleMessage.message.asString(),
                        wordRowAnimating = state.wordRowAnimating,
                        onEvent = onEvent
                    )
                }
            }
        }

        FalseKeyboard(
            isDarkMode = isDarkMode,
            enabled = state.loadingState != GameLoadingState.Loading &&
                    (state.gameState == WordSessionState.NotStarted ||
                            state.gameState == WordSessionState.InProgress),
            modifier = Modifier.fillMaxWidth(),
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
                navController = rememberNavController(),
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
                isDarkMode = true,
                onEvent = {}
            )
        }
    }
}