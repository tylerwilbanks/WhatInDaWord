package com.minutesock.dawordgame.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.dictionaryGraph(
    navController: NavController,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {

    navigation<NavigationGraph.DictionaryGraph>(
        startDestination = NavigationDestination.Dictionary
    ) {
        composable<NavigationDestination.Dictionary> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("\uD83D\uDEA7 Dictionary Screen is under construction. \uD83D\uDEA7")
            }
        }
    }
}