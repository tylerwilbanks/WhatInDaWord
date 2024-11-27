package com.minutesock.dawordgame.core.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.minutesock.dawordgame.feature.profile.presentation.ui.ProfileScreenHost

fun NavGraphBuilder.profileGraph(
    navController: NavController,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    navigation<NavigationGraph.ProfileGraph>(
        startDestination = NavigationDestination.Profile
    ) {
        composable<NavigationDestination.Profile> {
            ProfileScreenHost(
                modifier = modifier
            )
        }
    }
}