package com.minutesock.dawordgame.app.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.minutesock.dawordgame.core.navigation.NavigationDestination
import com.minutesock.dawordgame.core.navigation.navGraphRoute

@Composable
fun SideNavBar(
    navController: NavController,
    isDarkMode: Boolean
) {
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    val currentRouteIsBottomBarDestination by remember(currentDestination) {
        mutableStateOf(NavigationDestination.isBottomDestination(currentDestination))
    }

    var openDrawer by remember { mutableStateOf(true) }

    val openBackgroundColor = MaterialTheme.colorScheme.secondaryContainer
    val closedBackgroundColor = openBackgroundColor.copy(alpha = 0.0f)
    val backgroundColor by remember(openDrawer, isDarkMode) {
        mutableStateOf(
            if (openDrawer) openBackgroundColor else closedBackgroundColor
        )
    }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor
    )

    Row {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(color = animatedBackgroundColor)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(
                modifier = Modifier
                    .padding(4.dp)
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
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(Modifier.weight(1f))

            if (openDrawer) {
                NavigationDestination.bottomNavigationItems.forEach { item ->
                    val selected = currentDestination?.navGraphRoute() == item.navigationItem.graph.route
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(RectangleShape)
                            .clickable {
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
        VerticalDivider(
            thickness = 1.dp,
            color = if (openDrawer) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant
        )
    }
}