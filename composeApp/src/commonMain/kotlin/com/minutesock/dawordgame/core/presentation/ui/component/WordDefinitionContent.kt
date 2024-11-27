package com.minutesock.dawordgame.core.presentation.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.minutesock.dawordgame.core.domain.WordSessionState
import com.minutesock.dawordgame.core.domain.definition.WordEntry
import com.minutesock.dawordgame.feature.game.presentation.ui.component.WordDefinitionItem

@Composable
fun WordDefinitionContent(
    wordEntry: WordEntry,
    spoilerBlur: Dp = 0.dp,
    wordSessionState: WordSessionState = WordSessionState.Success,
    modifier: Modifier = Modifier.padding(horizontal = 20.dp)
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .blur(spoilerBlur)
    ) {
        items(wordEntry.definitions.size) { i ->
            val definition = wordEntry.definitions[i]
            Spacer(modifier = Modifier.height(8.dp))
            WordDefinitionItem(
                wordDefinition = definition,
                phonetic = wordEntry.phonetic,
                index = i,
                wordColor = if (wordSessionState == WordSessionState.Failure) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
            if (i < wordEntry.definitions.size - 1) {
                VerticalDivider()
            }
        }
    }
}