package com.minutesock.dawordgame.feature.dictionary.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.domain.GuessLetter
import com.minutesock.dawordgame.core.domain.GuessWordState
import com.minutesock.dawordgame.core.presentation.ui.component.LetterBox
import com.minutesock.dawordgame.core.theme.rememberDarkTheme
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryGuessWordRow
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryWordSessionInfo
import com.minutesock.dawordgame.feature.game.presentation.WordGameEvent
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.painterResource
import whatindaword.composeapp.generated.resources.Res
import whatindaword.composeapp.generated.resources.calendar_today
import whatindaword.composeapp.generated.resources.infinity

@Composable
fun DictionaryDetailSessionScreen(
    sessions: ImmutableList<DictionaryWordSessionInfo>,
) {
    val isDarkMode by rememberDarkTheme()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 20.dp,
            )
    ) {
        items(sessions.size) { i ->
            if (i == 0) {
                Spacer(modifier = Modifier.height(10.dp))
            }

            val session = sessions[i]

            DictionaryDetailSessionListItem(
                session = session,
                isDarkMode = isDarkMode
            )
            if (i < sessions.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (i == sessions.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
private fun DictionaryDetailSessionListItem(
    session: DictionaryWordSessionInfo,
    isDarkMode: Boolean
) {
    val iconId by remember {
        mutableStateOf(
            if (session.gameMode == GameMode.Daily) {
                Res.drawable.calendar_today
            } else {
                Res.drawable.infinity
            }
        )
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = session.displayDate,
                textAlign = TextAlign.Center
            )

            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(resource = iconId),
                contentDescription = null
            )
        }


        Spacer(modifier = Modifier.height(10.dp))

        session.wordRows.forEach {
            Row {
                DictionaryDetailSessionGuessWordRow(
                    wordRow = it,
                    isDarkMode = isDarkMode
                )
            }
        }
    }
}

@Composable
fun DictionaryDetailSessionGuessWordRow(
    wordRow: DictionaryGuessWordRow,
    isDarkMode: Boolean,
) {
    val textColor = when (wordRow.guessWord.state) {
        GuessWordState.Unused -> MaterialTheme.colorScheme.secondary
        GuessWordState.Editing -> MaterialTheme.colorScheme.secondary
        GuessWordState.Complete -> MaterialTheme.colorScheme.secondary
        GuessWordState.Correct -> MaterialTheme.colorScheme.primary
        GuessWordState.Failure -> MaterialTheme.colorScheme.error
    }

    val dummyEvent: (WordGameEvent) -> Unit = {}

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        wordRow.guessWord.letters.forEachIndexed { index: Int, letter: GuessLetter ->
            Row {
                LetterBox(
                    letter = letter,
                    guessWordState = wordRow.guessWord.state,
                    onEvent = dummyEvent,
                    flipAnimDelay = index * 50,
                    isFinalLetterInRow = false,
                    isDarkMode = isDarkMode
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = wordRow.displayTimestamp,
                textAlign = TextAlign.Right,
                color = textColor
            )
        }
    }
}