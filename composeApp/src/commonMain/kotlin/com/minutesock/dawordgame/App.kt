package com.minutesock.dawordgame

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.minutesock.dawordgame.core.navigation.NavDestination
import com.minutesock.dawordgame.core.navigation.dailyGraph
import com.minutesock.dawordgame.core.navigation.dictionaryGraph
import com.minutesock.dawordgame.core.navigation.infinityGraph
import com.minutesock.dawordgame.core.navigation.profileGraph
import com.minutesock.dawordgame.core.theme.AppTheme
import com.minutesock.dawordgame.core.uiutil.rememberWindowSizeBreakpoint
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme { isDarkMode: Boolean ->

        val windowSizeBreakpoint by rememberWindowSizeBreakpoint()

        // todo if windowSizeBreakpoint.Large do rail navigation otherwise, do bottom navigation

        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = NavDestination.Daily
        ) {
            dailyGraph(
                navController = navController,
                isDarkMode = isDarkMode,
            )

            infinityGraph(
                navController = navController,
                isDarkMode = isDarkMode
            )

            dictionaryGraph(
                navController = navController,
                isDarkMode = isDarkMode
            )

            profileGraph(
                navController = navController,
                isDarkMode = isDarkMode
            )
        }
    }
}