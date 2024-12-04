package com.minutesock.dawordgame.app.presentation.ui

import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import whatindaword.composeapp.generated.resources.Res
import whatindaword.composeapp.generated.resources.sock_large

@Composable
fun LogoScreen(
    typingSpeedMillis: Long = 35L,
    animationDuration: Long = 1_000L,
    onAnimatedFinished: () -> Unit
) {
    var targetSockScale by remember { mutableFloatStateOf(16f) }
    var displayedText by remember { mutableStateOf(" ") }

    val sockScale by animateFloatAsState(
        targetValue = targetSockScale,
        label = "sock expanding animation",
        animationSpec = tween(
            durationMillis = 750,
            easing = EaseOutBounce
        )
    )

    LaunchedEffect(Unit) {
        val fullText = "It's a sock." // todo-extract
        targetSockScale = 1f
        delay(750)
        for (i in fullText.indices) {
            displayedText = fullText.substring(0, i + 1)
            delay(typingSpeedMillis)
        }
    }

    LaunchedEffect(Unit) {
        delay(animationDuration)
        onAnimatedFinished()
    }

    Surface {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.scale(sockScale),
                    painter = painterResource(Res.drawable.sock_large),
                    contentDescription = "sock logo"
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = displayedText,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}