package com.minutesock.dawordgame.feature.game.presentation.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.navigation.NavigationDestination
import com.minutesock.dawordgame.core.theme.AppTheme
import com.minutesock.dawordgame.core.uiutil.bounceClick
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import whatindaword.composeapp.generated.resources.Res
import whatindaword.composeapp.generated.resources.calendar_today
import whatindaword.composeapp.generated.resources.infinity
import whatindaword.composeapp.generated.resources.what_in_da_word

@Composable
fun WordGameNotStartedScreen(
    modifier: Modifier = Modifier,
    completedGameCount: Int = 0,
    navController: NavController,
    gameMode: GameMode,
    onEvent: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val textColor by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colors.onSurface,
        targetValue = MaterialTheme.colors.primary,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val gameModeIconId by remember(gameMode) {
        mutableStateOf(
            when (gameMode) {
                GameMode.Daily -> Res.drawable.calendar_today
                GameMode.Infinity -> Res.drawable.infinity
            }
        )
    }

    Column(
        modifier = modifier.fillMaxSize().background(color = MaterialTheme.colors.background),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.what_in_da_word),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            color = textColor
        )
        Spacer(modifier = Modifier.height(50.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (completedGameCount < 5) {
                Button(
                    modifier = Modifier.bounceClick(),
                    onClick = {
                        navController.navigate(NavigationDestination.HowToPlay)
                    }
                ) {
                    Text(
                        text = "How To Play?",
                        color = MaterialTheme.colors.background,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }

            Button(
                modifier = Modifier.bounceClick(),
                onClick = {
                    onEvent(
//                        WordGameNotStartedEvent.OnGameBegin(
//                            gameMode = gameMode
//                        )
                    )
                }
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(resource = gameModeIconId),
                    tint = MaterialTheme.colors.background,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Play",
                    color = MaterialTheme.colors.background,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(resource = gameModeIconId),
                    tint = MaterialTheme.colors.background,
                    contentDescription = null
                )
            }
        }
    }

}

@Preview
@Composable
fun WordGameNotStartedScreenPreview() {
    AppTheme {
        Surface {
            WordGameNotStartedScreen(
                gameMode = GameMode.Infinity,
                navController = rememberNavController(),
                onEvent = {}
            )
        }
    }
}