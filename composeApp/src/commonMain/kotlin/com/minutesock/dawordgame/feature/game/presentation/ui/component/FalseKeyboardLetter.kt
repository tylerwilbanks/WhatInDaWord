package com.minutesock.dawordgame.feature.game.presentation.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minutesock.dawordgame.core.theme.md_theme_dark_onSecondary
import com.minutesock.dawordgame.core.theme.md_theme_light_onSecondary
import com.minutesock.dawordgame.feature.game.presentation.GuessKeyboardLetter
import com.minutesock.dawordgame.feature.game.presentation.WordGameEvent


@Composable
fun FalseKeyboardLetter(
    isDarkMode: Boolean,
    enabled: Boolean,
    guessKeyboardLetter: GuessKeyboardLetter,
    isWordRowAnimating: Boolean = false,
    onEvent: (WordGameEvent) -> Unit,
) {
    val isLetter by remember { mutableStateOf(guessKeyboardLetter.keyName.length == 1) }
    val sizeX by remember {
        mutableStateOf(
            if (isLetter) 35.dp else 50.dp
        )
    }

    val backgroundColor by animateColorAsState(
        targetValue = guessKeyboardLetter
            .displayColor(
                darkTheme = isDarkMode,
                defaultColor = if (isDarkMode) {
                    md_theme_dark_onSecondary
                } else {
                    md_theme_light_onSecondary
                }
            ),
        animationSpec = tween(3000), label = "letter background color"
    )

    val buttonEnabled by remember(isWordRowAnimating) {
        mutableStateOf(!(guessKeyboardLetter.keyName == "enter" && isWordRowAnimating))
    }

    IconButton(
        modifier = Modifier
            .padding(2.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10)
            )
            .size(sizeX, 55.dp)
            .clip(RoundedCornerShape(10))
            .indication(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true)
            ),

        colors = IconButtonDefaults.iconButtonColors(containerColor = backgroundColor),
        onClick = {
            if (!buttonEnabled || !enabled) {
                return@IconButton
            }

            when (guessKeyboardLetter.keyName) {
                "enter" -> {
                    onEvent.invoke(WordGameEvent.OnEnterPress)
                }

                "remove" -> {
                    onEvent.invoke(WordGameEvent.OnDeletePress)
                }

                else -> {
                    onEvent.invoke(
                        WordGameEvent.OnCharacterPress(
                            guessKeyboardLetter.character
                        )
                    )
                }
            }
        },
    ) {
        when (guessKeyboardLetter.keyName) {
            "enter" -> Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "enter",
                tint = MaterialTheme.colorScheme.primary
            )

            "remove" -> Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "remove",
                tint = if (isDarkMode) Color.White else Color.Black
            )

            else -> Text(
                textAlign = TextAlign.Center,
                text = guessKeyboardLetter.character.toString(),
                color = if (isDarkMode) Color.White else Color.Black,
                fontSize = 16.sp,
            )
        }
    }
}