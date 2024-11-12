package com.minutesock.dawordgame.feature.game.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.minutesock.dawordgame.core.domain.GuessLetter
import com.minutesock.dawordgame.core.domain.GuessLetterState
import com.minutesock.dawordgame.core.domain.GuessWord
import com.minutesock.dawordgame.core.domain.GuessWordState
import com.minutesock.dawordgame.core.uiutil.debouncedAction
import com.minutesock.dawordgame.feature.game.presentation.ui.component.WordRow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun HowToPlayScreen(
    navController: NavController,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
) {
    val word1 = GuessWord(
        state = GuessWordState.Complete,
        letters = listOf(
            GuessLetter('P', GuessLetterState.Absent),
            GuessLetter('L', GuessLetterState.Absent),
            GuessLetter('U', GuessLetterState.Absent),
            GuessLetter('M', GuessLetterState.Absent),
            GuessLetter('B', GuessLetterState.Absent)
        ).toImmutableList()
    )

    val word2 = GuessWord(
        state = GuessWordState.Complete,
        letters = listOf(
            GuessLetter('F', GuessLetterState.Absent),
            GuessLetter('I', GuessLetterState.Present),
            GuessLetter('G', GuessLetterState.Absent),
            GuessLetter('H', GuessLetterState.Present),
            GuessLetter('T', GuessLetterState.Absent)
        ).toImmutableList()
    )

    val word3 = GuessWord(
        state = GuessWordState.Complete,
        letters = listOf(
            GuessLetter('S', GuessLetterState.Absent),
            GuessLetter('H', GuessLetterState.Correct),
            GuessLetter('A', GuessLetterState.Absent),
            GuessLetter('R', GuessLetterState.Correct),
            GuessLetter('K', GuessLetterState.Absent)
        ).toImmutableList()
    )

    val word4 = GuessWord(
        state = GuessWordState.Complete,
        letters = listOf(
            GuessLetter('C', GuessLetterState.Correct),
            GuessLetter('H', GuessLetterState.Correct),
            GuessLetter('I', GuessLetterState.Correct),
            GuessLetter('R', GuessLetterState.Correct),
            GuessLetter('P', GuessLetterState.Correct)
        ).toImmutableList()
    )

    HowToPlayScreenContent(
        isDarkMode = isDarkMode,
        modifier = modifier,
        navController = navController,
        exampleWords = persistentListOf(word1, word2, word3, word4)
    )
}

@Composable
internal fun HowToPlayScreenContent(
    isDarkMode: Boolean,
    modifier: Modifier = Modifier,
    navController: NavController,
    wordLength: Int = 5,
    maxAttempts: Int = 6,
    exampleWords: ImmutableList<GuessWord>
) {
    val lazyListState = rememberLazyListState()

    val onCloseButtonClick = debouncedAction {
        navController.navigateUp()
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.onSecondary,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(10.dp),
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onCloseButtonClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "close",
                    )
                }
            }

            Text(
                text = "How To Play",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Text(
                text = "Guess the word in $maxAttempts tries.",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "\u2022 Each guess must be a valid $wordLength letter word.",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "\u2022 The color of the tiles will change to show how close your guess was to the word.",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Example:",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WordRow(
                    isDarkMode = isDarkMode,
                    guessWord = exampleWords[0],
                    guessLetters = exampleWords[0].letters,
                    message = "",
                    wordRowAnimating = false,
                    onEvent = {}
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("None ")
                        }
                        append("of these letters are found in the word.")
                    },
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                WordRow(
                    isDarkMode = isDarkMode,
                    guessWord = exampleWords[1],
                    guessLetters = exampleWords[1].letters,
                    message = "",
                    wordRowAnimating = false,
                    onEvent = {}
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("I ")
                        }
                        append("and ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("H ")
                        }
                        append("are found in the word, but not in the right spot.")
                    },
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                WordRow(
                    isDarkMode = isDarkMode,
                    guessWord = exampleWords[2],
                    guessLetters = exampleWords[2].letters,
                    message = "",
                    wordRowAnimating = false,
                    onEvent = {}
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("H ")
                        }
                        append("and ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("R ")
                        }
                        append("are found in the word, and are in the correct spot.")
                    },
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )

                WordRow(
                    isDarkMode = isDarkMode,
                    guessWord = exampleWords[3],
                    guessLetters = exampleWords[3].letters,
                    message = "",
                    wordRowAnimating = false,
                    onEvent = {}
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("All ")
                        }
                        append("letters are found in the word, and are in the correct spot. Well done!")
                    },
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
            }
        }
    }
}
