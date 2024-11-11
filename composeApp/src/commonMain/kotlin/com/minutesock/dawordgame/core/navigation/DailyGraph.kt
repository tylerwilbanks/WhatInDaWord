package com.minutesock.dawordgame.core.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.feature.game.presentation.ui.GameScreen
import com.minutesock.dawordgame.feature.game.presentation.ui.HowToPlayScreen
import com.minutesock.dawordgame.feature.game.presentation.ui.WordGameNotStartedScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.dailyGraph(
    navController: NavController,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {

    composable<NavigationDestination.Daily> {
        WordGameNotStartedScreen(
            navController = navController,
            gameMode = GameMode.Daily,
            onEvent = {}
        )
    }

    composable<NavigationDestination.HowToPlay> {
        HowToPlayScreen(
            navController = navController,
            isDarkMode = isDarkMode,
        )
    }

    composable<NavigationDestination.PlayGame>(
        typeMap = mapOf(
            typeOf<GameMode>() to GameMode.NavType
        )
    ) {
        val args = it.toRoute<NavigationDestination.PlayGame>()

        GameScreen(
            navController = navController,
            gameMode = args.gameMode,
            isDarkMode = isDarkMode
        )
    }
}