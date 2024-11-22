package com.minutesock.dawordgame.feature.game.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
    index: Int,
    wordColor: Color,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {
        Text(
            text = wordDefinition.word.uppercase(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = wordColor
        )
//        Text(text = wordDefinition.partOfSpeech ?: "", fontWeight = FontWeight.Light)
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(text = wordDefinition. ?: "")

        Text(text = wordDefinition.partOfSpeech, fontWeight = FontWeight.Bold)
        Text(text = "$index. ${wordDefinition.definition}")
        Spacer(modifier = Modifier.height(8.dp))
        wordDefinition.example?.let { example ->
            Text(text = "Example: $example")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}