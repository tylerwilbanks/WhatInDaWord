package com.minutesock.dawordgame.feature.game.presentation.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minutesock.dawordgame.feature.game.presentation.GuessLetter
import com.minutesock.dawordgame.feature.game.presentation.GuessWordState
import com.minutesock.dawordgame.feature.game.presentation.WordGameEvent

@Composable
fun LetterBox(
    letter: GuessLetter,
    guessWordState: GuessWordState,
    onEvent: (WordGameEvent) -> Unit,
    flipAnimDelay: Int,
    isFinalLetterInRow: Boolean
) {

    val animateColor by animateColorAsState(
        targetValue = if (letter.answered) letter.displayColor(MaterialTheme.colors.background) else MaterialTheme.colors.background,
        animationSpec = tween(
            durationMillis = 1250 / 2 + flipAnimDelay,
            delayMillis = 750 + flipAnimDelay
        ), label = "animateColor"
    )

    var flipRotation by remember { mutableStateOf(0f) }
    var buttonScale by remember {
        mutableStateOf(1.0f)
    }

    LaunchedEffect(letter.character) {
        animate(
            initialValue = 0.9f,
            targetValue = 1.0f,
            animationSpec =
            tween(
                durationMillis = 100,
                easing = LinearEasing
            )
        ) { value: Float, _: Float ->
            buttonScale = value
        }
    }

    LaunchedEffect(guessWordState) {
        if (guessWordState == GuessWordState.Unused || guessWordState == GuessWordState.Editing) {
            return@LaunchedEffect
        }
        animate(
            initialValue = 360f,
            targetValue = 0f,
            animationSpec =
            tween(
                delayMillis = flipAnimDelay,
                durationMillis = 1250,
                easing = LinearEasing
            )
        ) { value: Float, _: Float ->
            flipRotation = value
        }
        if (isFinalLetterInRow) {
            onEvent(WordGameEvent.OnAnsweredWordRowAnimationFinished)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .graphicsLayer {
                rotationX = flipRotation
            }
            .scale(buttonScale)
    ) {
        Card(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = animateColor,
                    shape = RoundedCornerShape(5.dp)
                )
                .graphicsLayer {
                    rotationX = flipRotation
                },
            border = BorderStroke(2.dp, MaterialTheme.colors.onBackground), // .outline),
            shape = RoundedCornerShape(5.dp),
            backgroundColor = animateColor
        ) {}
        Text(
            textAlign = TextAlign.Center,
            text = letter.displayCharacter,
            color = MaterialTheme.colors.onBackground,
            fontSize = 32.sp
        )
    }
}