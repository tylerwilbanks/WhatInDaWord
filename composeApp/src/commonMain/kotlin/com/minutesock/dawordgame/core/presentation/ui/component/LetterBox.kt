package com.minutesock.dawordgame.core.presentation.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minutesock.dawordgame.core.domain.GuessLetter
import com.minutesock.dawordgame.core.domain.GuessWordState
import com.minutesock.dawordgame.feature.game.presentation.WordGameEvent

@Composable
fun LetterBox(
    isDarkMode: Boolean,
    letter: GuessLetter,
    guessWordState: GuessWordState,
    onEvent: (WordGameEvent) -> Unit,
    flipAnimDelay: Int,
    isFinalLetterInRow: Boolean
) {

    val animateColor by animateColorAsState(
        targetValue = if (letter.answered) {
            letter.displayColor(
                isDarkMode,
                MaterialTheme.colorScheme.background
            )
        } else {
            MaterialTheme.colorScheme.background
        },
        animationSpec = tween(
            durationMillis = 750,
            delayMillis = flipAnimDelay
        ), label = "animateColor"
    )

    var flipRotation by remember { mutableFloatStateOf(0f) }
    var letterAlpha by remember { mutableFloatStateOf(1f) }
    var buttonScale by remember { mutableFloatStateOf(1.0f) }

    LaunchedEffect(letter.character) {
        animate(
            initialValue = 0.9f,
            targetValue = 1.0f,
            animationSpec = tween(
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
        letterAlpha = 0.0f
        animate(
            initialValue = 180f,
            targetValue = 0f,
            animationSpec = tween(
                delayMillis = flipAnimDelay,
                durationMillis = 750,
                easing = LinearEasing
            )
        ) { value: Float, _: Float ->
            flipRotation = value
            letterAlpha = (180f - value) / 180f
        }
        letterAlpha = 1f
        flipRotation = 0f
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
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(containerColor = animateColor),
        ) {}
        Text(
            modifier = Modifier.alpha(letterAlpha),
            textAlign = TextAlign.Center,
            text = letter.displayCharacter,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 32.sp
        )
    }
}