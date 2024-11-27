package com.minutesock.dawordgame.feature.dictionary.presentation.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.core.domain.definition.WordEntry
import com.minutesock.dawordgame.core.presentation.ui.component.WordDefinitionContent
import com.minutesock.dawordgame.core.util.ContinuousOption
import com.minutesock.dawordgame.core.util.capitalize
import com.minutesock.dawordgame.feature.dictionary.presentation.DictionaryDetailViewModel
import com.minutesock.dawordgame.feature.dictionary.presentation.ui.component.DictionaryDetailSessionScreen
import kotlin.enums.EnumEntries

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DictionaryDetailScreen(
    word: String,
    language: GameLanguage,
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    viewModel: DictionaryDetailViewModel = viewModel { DictionaryDetailViewModel(word, language) },
    modifier: Modifier = Modifier,
    tabs: EnumEntries<DictionaryDetailTab> = DictionaryDetailTab.entries
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

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
                if (wordEntry == null) {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    WordDefinitionContent(
                        wordEntry = wordEntry!!
                    )
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