package com.minutesock.dawordgame.core.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.feature.game.presentation.ui.GameScreen
import com.minutesock.dawordgame.feature.game.presentation.ui.HowToPlayScreen
import com.minutesock.dawordgame.feature.game.presentation.ui.WordGameNotStartedScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.infinityGraph(
    navController: NavController,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    navigation<NavigationGraph.InfinityGraph>(
        startDestination = NavigationDestination.Infinity
    ) {
        composable<NavigationDestination.Infinity> {
            WordGameNotStartedScreen(
                modifier = modifier,
                navController = navController,
                gameMode = GameMode.Infinity,
            )
        }

        composable<NavigationDestination.HowToPlay> {
            HowToPlayScreen(
                modifier = modifier,
                navController = navController,
                isDarkMode = isDarkMode,
                gameMode = GameMode.Infinity
            )
        }

        composable<NavigationDestination.PlayGame>(
            typeMap = mapOf(
                typeOf<GameMode>() to GameMode.NavType
            )
        ) {
            val args = it.toRoute<NavigationDestination.PlayGame>()

            GameScreen(
                modifier = modifier,
                navController = navController,
                gameMode = args.gameMode,
                isDarkMode = isDarkMode
            )
        }
    }
}