package com.minutesock.dawordgame.core.domain

import kotlinx.serialization.Serializable

sealed class NavDestination {
    @Serializable
    object HowToPlay

    @Serializable
    data class Game(
        val gameMode: GameMode
    )
}
