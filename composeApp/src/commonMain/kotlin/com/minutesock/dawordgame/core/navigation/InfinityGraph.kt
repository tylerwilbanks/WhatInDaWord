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

fun NavGraphBuilder.infinityGraph(
    navController: NavController,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {

    composable<NavDestination.Infinity> {
        WordGameNotStartedScreen(
            navController = navController,
            gameMode = GameMode.Infinity,
            onEvent = {}
        )
    }

    composable<NavDestination.HowToPlay> {
        HowToPlayScreen(
            navController = navController,
            isDarkMode = isDarkMode,
        )
    }

    composable<NavDestination.PlayGame>(
        typeMap = mapOf(
            typeOf<GameMode>() to GameMode.GameModeNavType
        )
    ) {
        val args = it.toRoute<NavDestination.PlayGame>()

        GameScreen(
            navController = navController,
            gameMode = args.gameMode,
            isDarkMode = isDarkMode
        )
    }
}