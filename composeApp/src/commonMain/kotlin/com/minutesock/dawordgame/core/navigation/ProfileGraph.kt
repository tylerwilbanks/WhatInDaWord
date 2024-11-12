package com.minutesock.dawordgame.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.profileGraph(
    navController: NavController,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    navigation<NavigationGraph.ProfileGraph>(
        startDestination = NavigationDestination.Profile
    ) {
        composable<NavigationDestination.Profile> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("\uD83D\uDEA7 Profile Screen is under construction. \uD83D\uDEA7")
            }
        }
    }
}