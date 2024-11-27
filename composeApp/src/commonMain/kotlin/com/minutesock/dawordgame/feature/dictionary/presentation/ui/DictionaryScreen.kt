package com.minutesock.dawordgame.feature.dictionary.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.minutesock.dawordgame.core.uiutil.blendColors
import com.minutesock.dawordgame.core.uiutil.shimmerEffect
import com.minutesock.dawordgame.core.util.formatDecimalSeparator
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryScreenEvent
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryState
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryViewModel
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryWordEntryListItem

@Composable
fun DictionaryScreenHost(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DictionaryViewModel = viewModel {
        DictionaryViewModel()
    }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DictionaryScreen(
        modifier = modifier,
        state = state,
        navController = navController,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DictionaryScreen(
    state: DictionaryState,
    navController: NavController,
    onEvent: (DictionaryScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val titleText by remember(state.unlockedWordCount, state.totalWordCount) {
        mutableStateOf(
            "Discovered words: ${state.unlockedWordCount.formatDecimalSeparator()} / ${state.totalWordCount.formatDecimalSeparator()}"
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    )
                    .padding(10.dp),
                text = titleText,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            state.headerItems.forEach { dictionaryHeaderItem ->
                stickyHeader {
                    CategoryHeader(text = dictionaryHeaderItem.char.toString())
                }
                items(dictionaryHeaderItem.listItems.size) { index: Int ->
                    CategoryItem(
                        item = dictionaryHeaderItem.listItems[index],
                        navController = navController,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp),
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
private fun CategoryItem(
    item: DictionaryWordEntryListItem,
    modifier: Modifier = Modifier,
    navController: NavController,
    onEvent: (DictionaryScreenEvent) -> Unit,
) {
    Text(
        text = item.word.uppercase(),
        fontSize = 16.sp,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                onEvent(
                    DictionaryScreenEvent.WordEntryClick(
                        navController = navController,
                        word = item.word
                    )
                )
            }
            .padding(16.dp)
            .shimmerEffect(
                color1 = MaterialTheme.colorScheme.background,
                color2 = blendColors(
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.primary,
                    0.15f
                ),
                duration = 3_000
            ),
        color = MaterialTheme.colorScheme.primary,
    )
}