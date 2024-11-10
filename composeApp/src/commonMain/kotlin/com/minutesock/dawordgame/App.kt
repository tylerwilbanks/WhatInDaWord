package com.minutesock.dawordgame

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.minutesock.dawordgame.core.domain.GameMode
import com.minutesock.dawordgame.core.navigation.NavDestination
import com.minutesock.dawordgame.core.theme.AppTheme
import com.minutesock.dawordgame.feature.game.presentation.ui.GameScreen
import com.minutesock.dawordgame.feature.game.presentation.ui.HowToPlayScreen
import com.minutesock.dawordgame.feature.game.presentation.ui.WordGameNotStartedScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.reflect.typeOf

@Composable
@Preview
fun App() {
    AppTheme { isDarkMode: Boolean ->

        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = NavDestination.Game(GameMode.Daily)
        ) {

            composable<NavDestination.Game>(
                typeMap = mapOf(
                    typeOf<GameMode>() to GameMode.GameModeNavType
                )
            ) {
                val args = it.toRoute<NavDestination.Game>()

                WordGameNotStartedScreen(
                    navController = navController,
                    gameMode = args.gameMode,
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
                val args = it.toRoute<NavDestination.Game>()

                GameScreen(
                    navController = navController,
                    gameMode = args.gameMode,
                    isDarkMode = isDarkMode
                )
            }
        }
    }
}