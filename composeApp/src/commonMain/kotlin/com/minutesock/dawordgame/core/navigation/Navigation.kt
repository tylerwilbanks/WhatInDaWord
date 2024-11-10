package com.minutesock.dawordgame.core.navigation

import com.minutesock.dawordgame.core.domain.GameMode
import kotlinx.serialization.Serializable

sealed class NavDestination {
    @Serializable
    data object HowToPlay : NavDestination()

    @Serializable
    data class Game(
        val gameMode: GameMode
    ) : NavDestination()

    @Serializable
    data class PlayGame(
        val gameMode: GameMode
    ) : NavDestination()
}
