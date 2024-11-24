package com.minutesock.dawordgame.feature.game.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.minutesock.dawordgame.core.domain.definition.WordDefinition

@Composable
fun WordDefinitionItem(
    wordDefinition: WordDefinition,
    phonetic: String?,
    index: Int,
    wordColor: Color,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(
                text = wordDefinition.word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = wordColor
            )
            Text(
                text = " • ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Light
            )
            phonetic?.let {
                Text(
                    text = phonetic,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = " • ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light
                )
            }
            Text(
                text = wordDefinition.partOfSpeech,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Text(text = "${index + 1}. ${wordDefinition.definition}")
        Spacer(modifier = Modifier.height(8.dp))
        wordDefinition.example?.let { example ->
            Text(
                text = "Example: $example",
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}