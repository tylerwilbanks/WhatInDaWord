package com.minutesock.dawordgame.feature.game.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minutesock.dawordgame.feature.game.presentation.FalseKeyboardKeys
import com.minutesock.dawordgame.feature.game.presentation.GuessKeyboardLetter
import com.minutesock.dawordgame.feature.game.presentation.WordGameEvent
import kotlinx.collections.immutable.ImmutableList


@Composable
fun FalseKeyboard(
    isDarkMode: Boolean,
    falseKeyboardKeys: FalseKeyboardKeys,
    onEvent: (WordGameEvent) -> Unit,
    modifier: Modifier = Modifier,
    isWordRowAnimating: Boolean = false,
) {
    Column(
        modifier = modifier.padding(bottom = 20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FalseKeyboardRow(isDarkMode = isDarkMode, row = falseKeyboardKeys.row1, onEvent = onEvent)
        FalseKeyboardRow(isDarkMode = isDarkMode, row = falseKeyboardKeys.row2, onEvent = onEvent)
        FalseKeyboardRow(
            isDarkMode = isDarkMode,
            row = falseKeyboardKeys.row3,
            onEvent = onEvent,
            isWordRowAnimating = isWordRowAnimating
        )
    }
}

@Composable
fun FalseKeyboardRow(
    isDarkMode: Boolean,
    row: ImmutableList<GuessKeyboardLetter>,
    onEvent: (WordGameEvent) -> Unit,
    isWordRowAnimating: Boolean = false
) {
    Row {
        row.forEach {
            FalseKeyboardLetter(
                isDarkMode = isDarkMode,
                onEvent = onEvent,
                guessKeyboardLetter = it,
                isWordRowAnimating = isWordRowAnimating
            )
        }
    }
}