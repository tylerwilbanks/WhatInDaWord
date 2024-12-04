package com.minutesock.dawordgame.app.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.minutesock.dawordgame.app.presentation.NavigationItem
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

    Column {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )

        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ) {

            NavigationDestination.bottomNavigationItems.forEach { item ->
                val selected = currentDestination?.navGraphRoute() == item.navigationItem.graph.route
                NavigationBarItem(
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
}