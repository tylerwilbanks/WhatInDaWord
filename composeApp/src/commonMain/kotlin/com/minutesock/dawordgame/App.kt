package com.minutesock.dawordgame

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.minutesock.dawordgame.app.presentation.ui.LogoScreen
import com.minutesock.dawordgame.app.presentation.ui.component.BottomNavBar
import com.minutesock.dawordgame.app.presentation.ui.component.SideNavBar
import com.minutesock.dawordgame.core.data.DataStoreManager
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
        var showLogoScreen by remember { mutableStateOf(DataStoreManager.showLogo) }
        AnimatedVisibility(
            visible = showLogoScreen,
            enter = fadeIn(),
            exit = fadeOut(
                animationSpec = tween(1_000)
            )
        ) {
            LogoScreen(
                animationDuration = 1_600L
            ) {
                showLogoScreen = false
            }
        }

        AnimatedVisibility(
            visible = !showLogoScreen,
            enter = fadeIn(
                animationSpec = tween(1_000)
            ),
            exit = fadeOut()
        ) {
            AppScreen(isDarkMode = isDarkMode)
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppScreen(isDarkMode: Boolean) {
    val windowSizeBreakpoint by rememberWindowSizeBreakpoint()
    val screenOrientation = getScreenOrientation()

    val showNavigationRail by remember(windowSizeBreakpoint, screenOrientation) {
        val windowIsWideEnough = when (windowSizeBreakpoint) {
            WindowSizeBreakpoint.ExtraSmall,
            WindowSizeBreakpoint.Small -> false

            WindowSizeBreakpoint.Medium,
            WindowSizeBreakpoint.Large,
            WindowSizeBreakpoint.ExtraLarge -> true
        }
        val showRail = windowIsWideEnough || screenOrientation == ScreenOrientation.Landscape
        mutableStateOf(showRail)
    }

    ToggleSystemStatusBar(showNavigationRail)

    val navController = rememberNavController()

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
        Row(
            modifier = Modifier
                .padding(padding)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            AnimatedVisibility(
                visible = showNavigationRail,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                SideNavBar(navController, isDarkMode)
            }
            SharedTransitionLayout {
                NavHost(
                    navController = navController,
                    startDestination = NavigationGraph.DailyGraph
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
                        sharedTransitionScope = this@SharedTransitionLayout,
                        isDarkMode = isDarkMode
                    )

                    profileGraph(
                        navController = navController,
                        isDarkMode = isDarkMode
                    )
                }
            }
        }
    }
}