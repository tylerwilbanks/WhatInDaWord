package com.minutesock.dawordgame.feature.dictionary.presentation.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.minutesock.dawordgame.core.domain.definition.WordEntry
import com.minutesock.dawordgame.core.navigation.NavigationDestination
import com.minutesock.dawordgame.core.presentation.ui.component.WordDefinitionContent
import com.minutesock.dawordgame.core.util.ContinuousOption
import com.minutesock.dawordgame.core.util.capitalize
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryDetailEvent
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryDetailState
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryDetailViewModel
import com.minutesock.dawordgame.feature.dictionary.presentation.ui.component.DictionaryDetailSessionScreen
import kotlin.enums.EnumEntries

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DictionaryDetailHost(
    args: NavigationDestination.DictionaryDetail,
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    viewModel: DictionaryDetailViewModel = viewModel { DictionaryDetailViewModel(args.word, args.language) },
    modifier: Modifier = Modifier
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    DictionaryDetailScreen(
        modifier = modifier,
        word = args.word,
        state = state,
        navController = navController,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DictionaryDetailScreen(
    word: String,
    state: DictionaryDetailState,
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    tabs: EnumEntries<DictionaryDetailTab> = DictionaryDetailTab.entries,
    onEvent: (DictionaryDetailEvent) -> Unit
) {

    val displayWord by remember(word) {
        mutableStateOf(word.capitalize())
    }

    var selectedTab by remember {
        mutableStateOf(DictionaryDetailTab.Definition)
    }

    val wordEntry: WordEntry? by remember(state.fetchState) {
        mutableStateOf(
            when (val fetchState = state.fetchState) {
                is ContinuousOption.Issue -> fetchState.data
                is ContinuousOption.Loading -> fetchState.data
                is ContinuousOption.Success -> fetchState.data
            }
        )
    }

    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = { navController.navigateUp() },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "navigate back"
                    )
                }
            }


            Box(
                Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                with(sharedTransitionScope) {
                    Text(
                        modifier = Modifier
                            .sharedElement(
                                state = sharedTransitionScope.rememberSharedContentState(key = word),
                                animatedVisibilityScope = animatedContentScope
                            )
                            .skipToLookaheadSize(),
                        text = displayWord,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 26.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }


            Box(
                Modifier.weight(1f)
            ) {}
        }

        TabRow(
            selectedTabIndex = selectedTab.ordinal
        ) {
            tabs.forEachIndexed { index: Int, _: DictionaryDetailTab ->
                Tab(
                    selected = selectedTab.ordinal == index,
                    onClick = {
                        selectedTab = DictionaryDetailTab.fromInt(index)
                    },
                    text = { Text(text = tabs[index].displayName) }
                )
            }
        }

        when (selectedTab) {
            DictionaryDetailTab.Definition -> {
                when (val result = state.fetchState) {
                    is ContinuousOption.Issue -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = result.issue.textRes.asString(),
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Tap below to view the definition on Dictionary.com", // todo extract
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    onEvent(DictionaryDetailEvent.PressDictionaryDotCom)
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next"
                                )
                                Spacer(modifier = Modifier.size(2.dp))
                                Text(
                                    text = "Definition" // todo extract
                                )
                            }
                        }
                    }

                    is ContinuousOption.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                            result.data?.let { wordEntry: WordEntry ->
                                println("I am loading and I got data! $wordEntry")
                                WordDefinitionContent(
                                    modifier = Modifier.padding(
                                        horizontal = 20.dp
                                    ),
                                    wordEntry = wordEntry,
                                )
                            }
                        }

                    }

                    is ContinuousOption.Success -> {
                        result.data?.let { wordEntry: WordEntry ->
                            WordDefinitionContent(
                                modifier = Modifier.padding(
                                    horizontal = 20.dp
                                ),
                                wordEntry = wordEntry,
                            )
                        }
                    }
                }
            }

            DictionaryDetailTab.Sessions -> {
                DictionaryDetailSessionScreen(
                    sessions = state.sessions
                )
            }
        }
    }
}

enum class DictionaryDetailTab {
    Definition,
    Sessions;

    val displayName: String
        get() = when (this) {
            Definition -> "Definition" // todo extract
            Sessions -> "Sessions" // todo extract
        }

    companion object {
        fun fromInt(value: Int) = DictionaryDetailTab.entries.first { it.ordinal == value }
    }
}