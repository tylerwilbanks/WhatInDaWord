package com.minutesock.dawordgame.app.presentation

import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.minutesock.dawordgame.core.navigation.NavigationDestination
import com.minutesock.dawordgame.core.navigation.navGraphRoute

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    val currentRouteIsBottomBarDestination by remember(currentDestination) {
        mutableStateOf(NavigationDestination.isBottomDestination(currentDestination))
    }

    BottomAppBar(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    ) {

        NavigationDestination.bottomNavigationItems.forEach { item ->
            val selected = currentDestination?.navGraphRoute() == item.navigationItem.graph.route
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
                    NavigationItem(
                        selected = selected,
                        icon = item.navigationItem.icon,
                        title = item.navigationItem.title
                    )
                },
                alwaysShowLabel = false
            )
        }
    }
}