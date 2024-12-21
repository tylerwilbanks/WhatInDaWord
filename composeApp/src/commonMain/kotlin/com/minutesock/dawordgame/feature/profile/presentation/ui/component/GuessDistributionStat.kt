package com.minutesock.dawordgame.feature.profile.presentation.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.minutesock.dawordgame.core.uiutil.blendColors
import com.minutesock.dawordgame.core.uiutil.shimmerEffect

@Composable
fun GuessDistributionStat(
    correctAttemptCount: Long,
    rowColor: Color,
    textColor: Color,
    attemptText: String,
    maxCorrectAttemptCount: Long,
    animDelay: Int,
    shouldShimmer: Boolean = false,
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val currentPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            correctAttemptCount.toFloat() / maxCorrectAttemptCount.coerceAtLeast(1)
        } else {
            0f
        },
        animationSpec = tween(
            1_000,
            animDelay
        ), label = ""
    )

    LaunchedEffect(correctAttemptCount, maxCorrectAttemptCount) {
        animationPlayed = true
    }

    Box(
        modifier = Modifier
            .width(340.dp)
            .height(24.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(currentPercent.value.coerceAtLeast(0.15f))
                .fillMaxHeight()
                .shimmerEffect(
                    color1 = rowColor,
                    color2 = if (shouldShimmer) {
                        blendColors(rowColor, Color.White, 0.4f)
                    } else {
                        rowColor
                    },
                    duration = 5_000
                )
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = attemptText,
                color = textColor,
                textAlign = TextAlign.Center
            )
            Text(
                text = (currentPercent.value * maxCorrectAttemptCount).toInt().toString(),
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}