package com.minutesock.dawordgame

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.minutesock.dawordgame.app.presentation.BottomNavBar
import com.minutesock.dawordgame.app.presentation.SideNavBar
import com.minutesock.dawordgame.core.navigation.NavigationGraph
import com.minutesock.dawordgame.core.navigation.dailyGraph
import com.minutesock.dawordgame.core.navigation.dictionaryGraph
import com.minutesock.dawordgame.core.navigation.infinityGraph
import com.minutesock.dawordgame.core.navigation.profileGraph
import com.minutesock.dawordgame.core.theme.AppTheme
import com.minutesock.dawordgame.core.uiutil.WindowSizeBreakpoint
import com.minutesock.dawordgame.core.uiutil.rememberWindowSizeBreakpoint
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme { isDarkMode: Boolean ->

        val windowSizeBreakpoint by rememberWindowSizeBreakpoint()

        val showNavigationRail by remember(windowSizeBreakpoint) {
            mutableStateOf(
                when (windowSizeBreakpoint) {
                    WindowSizeBreakpoint.ExtraSmall,
                    WindowSizeBreakpoint.Small -> false

                    WindowSizeBreakpoint.Medium,
                    WindowSizeBreakpoint.Large,
                    WindowSizeBreakpoint.ExtraLarge -> true
                }
            )
        }

        val navController = rememberNavController()

        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            AnimatedVisibility(
                visible = showNavigationRail,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                SideNavBar(navController)
            }
            Scaffold(
                bottomBar = {
                    AnimatedVisibility(
                        visible = !showNavigationRail,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        BottomNavBar(navController)
                    }
                }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = NavigationGraph.DailyGraph
                ) {
                    dailyGraph(
                        modifier = Modifier.padding(padding),
                        navController = navController,
                        isDarkMode = isDarkMode,
                    )

                    infinityGraph(
                        modifier = Modifier.padding(padding),
                        navController = navController,
                        isDarkMode = isDarkMode
                    )

                    dictionaryGraph(
                        modifier = Modifier.padding(padding),
                        navController = navController,
                        isDarkMode = isDarkMode
                    )

                    profileGraph(
                        modifier = Modifier.padding(padding),
                        navController = navController,
                        isDarkMode = isDarkMode
                    )
                }
            }
        }
    }
}