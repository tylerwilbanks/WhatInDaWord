package com.minutesock.dawordgame.core.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.minutesock.dawordgame.feature.dictionary.presentation.ui.DictionaryScreenHost

fun NavGraphBuilder.dictionaryGraph(
    navController: NavController,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {

    navigation<NavigationGraph.DictionaryGraph>(
        startDestination = NavigationDestination.Dictionary
    ) {
        composable<NavigationDestination.Dictionary> {
            DictionaryScreenHost(
                navController = navController,
                modifier = modifier
            )
        }
    }
}