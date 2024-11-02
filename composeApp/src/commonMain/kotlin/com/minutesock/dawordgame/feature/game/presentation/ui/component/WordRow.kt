package com.minutesock.dawordgame.feature.game.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.minutesock.dawordgame.core.domain.GuessLetter
import com.minutesock.dawordgame.core.domain.GuessWord
import com.minutesock.dawordgame.core.domain.GuessWordState
import com.minutesock.dawordgame.core.uiutil.ShakeConfig
import com.minutesock.dawordgame.core.uiutil.rememberShakeController
import com.minutesock.dawordgame.core.uiutil.shake
import com.minutesock.dawordgame.feature.game.presentation.GuessWordError
import com.minutesock.dawordgame.feature.game.presentation.WordGameEvent
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import whatindaword.composeapp.generated.resources.Res
import whatindaword.composeapp.generated.resources.what_in_da_word


@Composable
fun WordRow(
    guessWord: GuessWord,
    guessLetters: ImmutableList<GuessLetter>,
    message: String?,
    wordRowAnimating: Boolean,
    onEvent: (WordGameEvent) -> Unit
) {
    val shakeController = rememberShakeController()
    val defaultMessage = stringResource(Res.string.what_in_da_word)
    LaunchedEffect(message) {
        if (wordRowAnimating) {
            return@LaunchedEffect
        }
        if (
            message != defaultMessage &&
            guessWord.state == GuessWordState.Editing &&
            guessWord.errorState != GuessWordError.None
        ) {
            shakeController.shake(ShakeConfig.no())
        }

        when (guessWord.state) {
            GuessWordState.Correct -> shakeController.shake(ShakeConfig.yes())
            GuessWordState.Failure -> shakeController.shake(ShakeConfig.no())
            else -> Unit
        }
    }

    Row(
        modifier = Modifier
            .padding(5.dp)
            .shake(
                shakeController = shakeController,
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        guessLetters.forEachIndexed { index: Int, guessLetter: GuessLetter ->
            LetterBox(
                letter = guessLetter,
                guessWordState = guessWord.state,
                onEvent = onEvent,
                flipAnimDelay = index * 200,
                isFinalLetterInRow = index + 1 == guessLetters.size
            )
        }
    }
}