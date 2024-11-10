package com.minutesock.dawordgame.core.navigation

import com.minutesock.dawordgame.core.domain.GameMode
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import whatindaword.composeapp.generated.resources.Res
import whatindaword.composeapp.generated.resources.book
import whatindaword.composeapp.generated.resources.calendar_today
import whatindaword.composeapp.generated.resources.infinity
import whatindaword.composeapp.generated.resources.person

sealed class NavDestination {

    /* ------- daily destinations ------- */

    @Serializable
    data object Daily : NavDestination()

    /* ------- infinity destinations ------- */

    @Serializable
    data object Infinity : NavDestination()

    /* ------- daily and infinity destinations ------- */

    @Serializable
    data object HowToPlay : NavDestination()

    @Serializable
    data class PlayGame(
        val gameMode: GameMode
    ) : NavDestination()

    /* ------- dictionary destinations ------- */

    @Serializable
    data object Dictionary : NavDestination()

    /* ------- profile destinations ------- */

    @Serializable
    data object Profile : NavDestination()
}

class NavigationItem(
    val icon: DrawableResource,
    val startDestination: NavDestination,
    val title: String
)

sealed class BottomNavigationDestination(
    val navigationItem: NavigationItem
) {
    data object Daily : BottomNavigationDestination(
        NavigationItem(
            icon = Res.drawable.calendar_today,
            startDestination = NavDestination.Daily,
            title = "Daily" // todo extract string resource
        )
    )

    data object Infinity : BottomNavigationDestination(
        NavigationItem(
            icon = Res.drawable.infinity,
            startDestination = NavDestination.Infinity,
            title = "Infinity" // todo extract string resource
        )
    )

    data object Dictionary : BottomNavigationDestination(
        NavigationItem(
            icon = Res.drawable.book,
            startDestination = NavDestination.Dictionary,
            title = "Dictionary" // todo extract string resource
        )
    )

    data object Profile : BottomNavigationDestination(
        NavigationItem(
            icon = Res.drawable.person,
            startDestination = NavDestination.Profile,
            title = "Profile" // todo extract string resource
        )
    )
}
