package com.minutesock.dawordgame.feature.game.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minutesock.dawordgame.PlatformType
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.WordSelection
import com.minutesock.dawordgame.core.domain.WordSession
import com.minutesock.dawordgame.core.domain.WordSessionState
import com.minutesock.dawordgame.core.domain.definition.WordEntry
import com.minutesock.dawordgame.core.presentation.ui.component.WordDefinitionContent
import com.minutesock.dawordgame.core.theme.AppTheme
import com.minutesock.dawordgame.core.uiutil.TextRes
import com.minutesock.dawordgame.core.util.ContinuousOption
import com.minutesock.dawordgame.core.util.capitalize
import com.minutesock.dawordgame.feature.game.presentation.GameScreenState
import com.minutesock.dawordgame.feature.game.presentation.GameStatsState
import com.minutesock.dawordgame.feature.game.presentation.GameTitleMessage
import com.minutesock.dawordgame.feature.game.presentation.GameViewModelState
import com.minutesock.dawordgame.feature.game.presentation.WordGameStatsEvent
import com.minutesock.dawordgame.getPlatform
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whatindaword.composeapp.generated.resources.Res
import whatindaword.composeapp.generated.resources.hide
import whatindaword.composeapp.generated.resources.reveal
import whatindaword.composeapp.generated.resources.share
import whatindaword.composeapp.generated.resources.visibility
import whatindaword.composeapp.generated.resources.visibility_off

@Composable
fun PlayGameStatsScreen(
    gameState: GameViewModelState,
    statsState: GameStatsState,
    onEvent: (WordGameStatsEvent) -> Unit,
) {

    var shareEnabled by remember(gameState.gameState) {
        mutableStateOf(gameState.gameState.isGameOver)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var revealSpoiler by remember(gameState.gameState) {
        mutableStateOf(gameState.gameState == WordSessionState.Success)
    }

    val spoilerBlur by remember(revealSpoiler) {
        mutableStateOf(if (revealSpoiler) 0.dp else 10.dp)
    }

    val spoilerButtonEnabled by remember(gameState.gameState) {
        mutableStateOf(gameState.gameState.isGameOver)
    }

    val textResultColor = if (gameState.gameTitleMessage.isError || gameState.gameState == WordSessionState.Failure) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { onEvent(WordGameStatsEvent.PressExit) }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    text = gameState.gameTitleMessage.message.asString(),
                    color = textResultColor
                )
            }
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            Scaffold(
                snackbarHost = {
                    SnackbarHost(snackbarHostState)
                },
                topBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            modifier = Modifier
                                .border(
                                    border = BorderStroke(
                                        2.dp,
                                        MaterialTheme.colorScheme.secondary
                                    ),
                                    shape = RoundedCornerShape(corner = CornerSize(10.dp))
                                )
                                .blur(spoilerBlur)
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 25.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium,
                            text = gameState.mysteryWord.word.capitalize(),
                            color = textResultColor
                        )
                    }
                },
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                revealSpoiler = !revealSpoiler
                            },
                            enabled = spoilerButtonEnabled,
                        ) {
                            Icon(
                                painterResource(if (revealSpoiler) Res.drawable.visibility_off else Res.drawable.visibility),
                                contentDescription = "Show/Hide"
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(
                                text = if (revealSpoiler) {
                                    stringResource(Res.string.hide)
                                } else {
                                    stringResource(Res.string.reveal)
                                }
                            )
                        }
                        Button(
                            onClick = {
                                shareEnabled = false
                                onEvent(WordGameStatsEvent.PressShare)
                                scope.launch {
                                    when (getPlatform().type) {
                                        PlatformType.Android -> Unit
                                        PlatformType.Desktop -> {
                                            snackbarHostState.showSnackbar(
                                                message = "✅ Copied to clipboard!" // todo extract
                                            )
                                        }

                                        PlatformType.Native -> Unit
                                    }
                                    shareEnabled = true
                                }
                            },
                            enabled = shareEnabled,
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(
                                text = stringResource(Res.string.share)
                            )
                        }

                        val debug = false
                        if (debug && gameState.gameMode == GameMode.Daily) {
                            Button(
                                onClick = {
                                    onEvent(WordGameStatsEvent.DeleteAndRestartDailyGame)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Restart"
                                )
                                Spacer(modifier = Modifier.size(2.dp))
                                Text(text = "Reset")
                            }
                        }

                        if (gameState.gameMode == GameMode.Infinity) {
                            Button(
                                onClick = {
                                    onEvent(WordGameStatsEvent.NextInfinitySession)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                enabled = gameState.gameState.isGameOver
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next"
                                )
                                Spacer(modifier = Modifier.size(2.dp))
                                Text(
                                    text = "Next" // todo extract
                                )
                            }
                        }
                    }
                },
                content = { padding ->
                    when (val result = statsState.fetchState) {
                        is ContinuousOption.Issue -> {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(padding).blur(spoilerBlur),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = result.issue.textRes.asString(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Tap below to view the definition on Dictionary.com", // todo extract
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        onEvent(WordGameStatsEvent.PressDictionaryDotCom)
                                    },
                                    enabled = spoilerButtonEnabled
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Next"
                                    )
                                    Spacer(modifier = Modifier.size(2.dp))
                                    Text(
                                        text = "Definition" // todo extract
                                    )
                                }
                            }
                        }

                        is ContinuousOption.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize().padding(padding),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                            result.data?.let { wordEntry: WordEntry ->
                                WordDefinitionContent(
                                    modifier = Modifier.padding(
                                        top = padding.calculateTopPadding(),
                                        bottom = padding.calculateBottomPadding(),
                                        start = 20.dp,
                                        end = 20.dp
                                    ),
                                    wordEntry = wordEntry,
                                    wordSessionState = gameState.gameState,
                                    spoilerBlur = spoilerBlur
                                )
                            }
                        }

                        is ContinuousOption.Success -> {
                            result.data?.let { wordEntry: WordEntry ->
                                WordDefinitionContent(
                                    modifier = Modifier.padding(
                                        top = padding.calculateTopPadding(),
                                        bottom = padding.calculateBottomPadding(),
                                        start = 20.dp,
                                        end = 20.dp
                                    ),
                                    wordEntry = wordEntry,
                                    wordSessionState = gameState.gameState,
                                    spoilerBlur = spoilerBlur
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun PlayGameStatsScreenPreview() {
    AppTheme {
        Surface {
            PlayGameStatsScreen(
                gameState = GameViewModelState(
                    wordSession = WordSession(
                        date = Clock.System.todayIn(TimeZone.currentSystemDefault()),
                        mysteryWord = "Jumby",
                        language = GameLanguage.English,
                        maxAttempts = 6,
                        gameMode = GameMode.Daily,
                        state = WordSessionState.Success
                    ),
                    screenState = GameScreenState.Stats,
                    gameTitleMessage = GameTitleMessage(
                        message = TextRes.Raw(
                            value = "Wow great job you solved it wow great job wow!!!!!!!!!"
                        )
                    ),
                    mysteryWord = WordSelection(id = 0, word = "Jumby", language = GameLanguage.English)
                ),
                statsState = GameStatsState(),
                onEvent = {},
            )
        }
    }
}
