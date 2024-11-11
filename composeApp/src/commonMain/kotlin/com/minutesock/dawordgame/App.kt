package com.minutesock.dawordgame

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.minutesock.dawordgame.core.navigation.NavigationDestination
import com.minutesock.dawordgame.core.navigation.NavigationGraph
import com.minutesock.dawordgame.core.navigation.dailyGraph
import com.minutesock.dawordgame.core.navigation.dictionaryGraph
import com.minutesock.dawordgame.core.navigation.infinityGraph
import com.minutesock.dawordgame.core.navigation.profileGraph
import com.minutesock.dawordgame.core.theme.AppTheme
import com.minutesock.dawordgame.core.uiutil.rememberWindowSizeBreakpoint
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme { isDarkMode: Boolean ->

        val windowSizeBreakpoint by rememberWindowSizeBreakpoint()

        // todo if windowSizeBreakpoint.Medium >= do rail navigation otherwise, do bottom navigation

        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.primary
                ) {

                    val navStackBackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navStackBackEntry?.destination

                    val currentRouteIsBottomBarDestination by remember(currentDestination) {
                        mutableStateOf(NavigationDestination.isBottomDestination(currentDestination))
                    }

                    NavigationDestination.bottomNavigationItems.forEach { item ->
                        val selected =
                            currentDestination?.parent?.route?.substringAfterLast(".") == item.navigationItem.graph::class.simpleName
                        BottomNavigationItem(
                            selected = selected,
                            onClick = {
                                if (!item.navigationItem.graph.isDestination(currentDestination)) {
                                    navController.navigate(item.navigationItem.graph) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        painter = painterResource(item.navigationItem.icon),
                                        tint = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.secondaryVariant,
                                        contentDescription = item.navigationItem.title
                                    )
                                    Text(
                                        modifier = Modifier.animateContentSize(),
                                        text = item.navigationItem.title,
                                        color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.secondaryVariant,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = if (selected) 16.sp else 0.sp
                                    )
                                }
                            },
                            alwaysShowLabel = false
                        )
                    }
                }
            }
        ) {
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