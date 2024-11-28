package com.minutesock.dawordgame.core.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.minutesock.dawordgame.core.domain.GameLanguage
import com.minutesock.dawordgame.feature.dictionary.presentation.ui.DictionaryDetailHost
import com.minutesock.dawordgame.feature.dictionary.presentation.ui.DictionaryScreenHost
import kotlin.reflect.typeOf

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.dictionaryGraph(
    navController: NavController,
    sharedTransitionScope: SharedTransitionScope,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    navigation<NavigationGraph.DictionaryGraph>(
        startDestination = NavigationDestination.Dictionary
    ) {
        composable<NavigationDestination.Dictionary>(

        ) {
            DictionaryScreenHost(
                navController = navController,
                modifier = modifier,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable
            )
        }

        composable<NavigationDestination.DictionaryDetail>(
            typeMap = mapOf(
                typeOf<GameLanguage>() to GameLanguage.NavType
            )
        ) {
            val args = it.toRoute<NavigationDestination.DictionaryDetail>()
            DictionaryDetailHost(
                modifier = modifier,
                navController = navController,
                args = args,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable
            )
        }
    }
}