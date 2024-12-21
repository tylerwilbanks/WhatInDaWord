package com.minutesock.dawordgame.feature.profile.presentation.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minutesock.dawordgame.feature.profile.presentation.GuessDistributionState

@Composable
fun GuessDistributionPanel(
    guessDistributionState: GuessDistributionState
) {
    Card(
        modifier = Modifier
            .width(400.dp)
            .padding(10.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(5.dp)
            ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Guess Distribution", // todo extract string resource
                fontSize = 26.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(10.dp))
            guessDistributionState.guessDistributions.forEachIndexed { index, guessDistribution ->
                GuessDistributionStat(
                    correctAttemptCount = guessDistribution.wordSessionCount,
                    rowColor = lerp(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary,
                        (index + 1) / guessDistributionState.guessDistributions.size.toFloat()
                    ),
                    textColor = lerp(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer,
                        (index + 1) / guessDistributionState.guessDistributions.size.toFloat()
                    ),
                    maxCorrectAttemptCount = guessDistributionState.totalSessionCount,
                    animDelay = (guessDistributionState.guessDistributions.size * 100) - (index * 100),
                    attemptText = guessDistribution.attemptCount.toString(),
                    shouldShimmer = guessDistribution.wordSessionCount != 0L
                )
                Spacer(modifier = Modifier.height(5.dp))
            }

            GuessDistributionStat(
                correctAttemptCount = guessDistributionState.failedGameSessionsCount,
                rowColor = MaterialTheme.colorScheme.errorContainer,
                textColor = MaterialTheme.colorScheme.error,
                maxCorrectAttemptCount = guessDistributionState.totalSessionCount,
                animDelay = 0,
                attemptText = "X"
            )
        }
    }
}