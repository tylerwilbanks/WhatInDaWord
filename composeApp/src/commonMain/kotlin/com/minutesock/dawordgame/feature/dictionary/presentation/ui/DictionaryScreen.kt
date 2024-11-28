package com.minutesock.dawordgame.feature.dictionary.presentation.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.runtime.DisposableEffect
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.navigation.NavigationDestination
import com.minutesock.dawordgame.core.util.capitalize
import com.minutesock.dawordgame.core.util.formatDecimalSeparator
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryScreenEvent
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryState
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryViewModel
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryWordEntryListItem

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DictionaryScreenHost(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    viewModel: DictionaryViewModel = viewModel {
        DictionaryViewModel()
    }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.updateListIfNeeded()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    DictionaryScreen(
        modifier = modifier,
        state = state,
        navController = navController,
        onEvent = viewModel::onEvent,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun DictionaryScreen(
    state: DictionaryState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    navController: NavController,
    onEvent: (DictionaryScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val titleText by remember(state.unlockedWordCount, state.totalWordCount) {
        mutableStateOf(
            "Discovered words: ${state.unlockedWordCount.formatDecimalSeparator()} / ${state.totalWordCount.formatDecimalSeparator()}"
        )
    }

    // todo create empty state

    Scaffold(
        modifier = modifier,
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
                        language = state.language,
                        navController = navController,
                        onEvent = onEvent,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun CategoryItem(
    item: DictionaryWordEntryListItem,
    language: GameLanguage,
    modifier: Modifier = Modifier,
    navController: NavController,
    onEvent: (DictionaryScreenEvent) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    with(sharedTransitionScope) {
        Text(
            text = item.word.capitalize(),
            fontSize = 16.sp,
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onEvent(
                        DictionaryScreenEvent.WordEntryClick(
                            navController = navController,
                            args = NavigationDestination.DictionaryDetail(
                                word = item.word.lowercase(),
                                language = language
                            )
                        )
                    )
                }
                .padding(16.dp)
                .sharedElement(
                    state = sharedTransitionScope.rememberSharedContentState(key = item.word),
                    animatedVisibilityScope = animatedContentScope
                ),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}