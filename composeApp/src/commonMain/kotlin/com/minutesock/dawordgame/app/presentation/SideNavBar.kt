package com.minutesock.dawordgame.app.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.minutesock.dawordgame.core.navigation.NavigationDestination
import com.minutesock.dawordgame.core.navigation.navGraphRoute

@Composable
fun SideNavBar(
    navController: NavController
) {

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    val currentRouteIsBottomBarDestination by remember(currentDestination) {
        mutableStateOf(NavigationDestination.isBottomDestination(currentDestination))
    }

    var openDrawer by remember { mutableStateOf(true) }

    val openBackgroundColor = MaterialTheme.colors.primarySurface
    val closedBackgroundColor = openBackgroundColor.copy(alpha = 0.0f)
    val backgroundColor by remember(openDrawer) {
        mutableStateOf(
            if (openDrawer) openBackgroundColor else closedBackgroundColor
        )
    }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor
    )


    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        openBackgroundColor,
                        Color.Transparent
                    )
                )
            )
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        IconButton(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
                .animateContentSize()
                .zIndex(99f),
            onClick = {
                openDrawer = !openDrawer
            }
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Open/Close Drawer",
                tint = MaterialTheme.colors.secondaryVariant
            )
        }

        Spacer(Modifier.weight(1f))

        if (openDrawer) {
            NavigationDestination.bottomNavigationItems.forEach { item ->
                val selected = currentDestination?.navGraphRoute() == item.navigationItem.graph.route
                IconButton(
                    modifier = Modifier.padding(8.dp),
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
                    }
                ) {
                    NavigationItem(
                        selected = selected,
                        icon = item.navigationItem.icon,
                        title = item.navigationItem.title,
                        alwaysShowLabel = true
                    )
                }
            }
        }
    }
}