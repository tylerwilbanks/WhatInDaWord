package com.minutesock.dawordgame.core.navigation

import androidx.navigation.NavDestination
import com.minutesock.dawordgame.core.domain.GameMode
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import whatindaword.composeapp.generated.resources.Res
import whatindaword.composeapp.generated.resources.book
import whatindaword.composeapp.generated.resources.calendar_today
import whatindaword.composeapp.generated.resources.infinity
import whatindaword.composeapp.generated.resources.person

@Serializable
sealed class NavigationDestination {

    /* ------- daily destinations ------- */

    @Serializable
    data object Daily : NavigationDestination()

    /* ------- infinity destinations ------- */

    @Serializable
    data object Infinity : NavigationDestination()

    /* ------- daily and infinity destinations ------- */

    @Serializable
    data object HowToPlay : NavigationDestination()

    @Serializable
    data class PlayGame(
        val gameMode: GameMode
    ) : NavigationDestination()

    /* ------- dictionary destinations ------- */

    @Serializable
    data object Dictionary : NavigationDestination()

    /* ------- profile destinations ------- */

    @Serializable
    data object Profile : NavigationDestination()

    fun isDestination(destination: NavDestination?): Boolean {
        return this::class.simpleName == destination?.toString()?.substringAfterLast(".")
    }

    companion object {
        fun isBottomDestination(destination: NavDestination?): Boolean {
            val destinationName = destination?.toString()?.substringAfterLast(".")
            return when (destinationName) {
                Daily::class.simpleName -> true
                Infinity::class.simpleName -> true
                Dictionary::class.simpleName -> true
                Profile::class.simpleName -> true
                else -> false
            }
        }

        val bottomNavigationItems = persistentListOf(
            BottomNavigationDestination.Daily,
            BottomNavigationDestination.Infinity,
            BottomNavigationDestination.Dictionary,
            BottomNavigationDestination.Profile
        )
    }
}

@Serializable
sealed class NavigationGraph {

    @Serializable
    data object DailyGraph : NavigationGraph()

    @Serializable
    data object InfinityGraph : NavigationGraph()

    @Serializable
    data object DictionaryGraph : NavigationGraph()

    @Serializable
    data object ProfileGraph : NavigationGraph()

    fun isDestination(destination: NavDestination?): Boolean {
        return this::class.simpleName == destination?.toString()?.substringAfterLast(".")
    }
}

class NavigationItem(
    val icon: DrawableResource,
    val graph: NavigationGraph,
    val title: String
)

sealed class BottomNavigationDestination(
    val navigationItem: NavigationItem
) {
    data object Daily : BottomNavigationDestination(
        NavigationItem(
            icon = Res.drawable.calendar_today,
            graph = NavigationGraph.DailyGraph,
            title = "Daily" // todo extract string resource
        )
    )

    data object Infinity : BottomNavigationDestination(
        NavigationItem(
            icon = Res.drawable.infinity,
            graph = NavigationGraph.InfinityGraph,
            title = "Infinity" // todo extract string resource
        )
    )

    data object Dictionary : BottomNavigationDestination(
        NavigationItem(
            icon = Res.drawable.book,
            graph = NavigationGraph.DictionaryGraph,
            title = "Dictionary" // todo extract string resource
        )
    )

    data object Profile : BottomNavigationDestination(
        NavigationItem(
            icon = Res.drawable.person,
            graph = NavigationGraph.ProfileGraph,
            title = "Profile" // todo extract string resource
        )
    )
}
