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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.WordSelection
import com.minutesock.dawordgame.core.domain.WordSession
import com.minutesock.dawordgame.core.domain.WordSessionState
import com.minutesock.dawordgame.core.theme.AppTheme
import com.minutesock.dawordgame.core.uiutil.TextRes
import com.minutesock.dawordgame.feature.game.presentation.GameScreenState
import com.minutesock.dawordgame.feature.game.presentation.GameStatsState
import com.minutesock.dawordgame.feature.game.presentation.GameTitleMessage
import com.minutesock.dawordgame.feature.game.presentation.GameViewModelState
import com.minutesock.dawordgame.feature.game.presentation.WordGameStatsEvent
import com.minutesock.dawordgame.feature.game.presentation.ui.component.WordDefinitionItem
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
import whatindaword.composeapp.generated.resources.what_in_da_word

@Composable
fun PlayGameStatsScreen(
    gameState: GameViewModelState,
    statsState: GameStatsState,
    onEvent: (WordGameStatsEvent) -> Unit,
) {

    var shareEnabled by remember(gameState.gameState) { mutableStateOf(gameState.gameState.isGameOver) }

//    val launcher =
//        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            shareEnabled = true
//            onEvent(WordEventStats.OnShareChooserPresented)
//        }

    val title = stringResource(Res.string.what_in_da_word)

//    LaunchedEffect(state.shareText) {
//        state.shareText?.let { shareText ->
//            launcher.launch("${title}\n$shareText".shareExternal())
//        }
//    }

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
                topBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
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
                            text = gameState.mysteryWord.word.uppercase(),
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
                            },
                            enabled = shareEnabled,
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(
                                text = stringResource(Res.string.share)
                            )
                        }

                        val debug = true // todo check for debug build here
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
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                enabled = gameState.gameState.isGameOver
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Next"
                                )
                                Spacer(modifier = Modifier.size(2.dp))
                                Text(
                                    text = "Next"
                                )
                            }
                        }
                    }
                },
                content = { padding ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(spoilerBlur)
                            .padding(
                                bottom = padding.calculateBottomPadding(),
                                top = padding.calculateTopPadding(),
                                start = 20.dp,
                                end = 20.dp
                            )
                    ) {
                        items(statsState.wordEntry?.definitions?.size ?: 0) { i ->
                            val wordEntry = statsState.wordEntry!!
                            val definition = wordEntry.definitions[i]
                            if (i > 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            WordDefinitionItem(
                                wordDefinition = definition,
                                phonetic = wordEntry.phonetic,
                                index = i,
                                wordColor = if (gameState.gameState == WordSessionState.Failure) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.primary
                                }
                            )
                            if (i < statsState.wordEntry!!.definitions.size - 1) {
                                VerticalDivider()
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
