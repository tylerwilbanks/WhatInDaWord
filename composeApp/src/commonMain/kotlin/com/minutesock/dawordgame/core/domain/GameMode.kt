package com.minutesock.dawordgame.core.domain

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.Serializable

@Serializable
enum class GameMode {
    Daily,
    Infinity;

    val dbName: String
        get() {
            return when (this) {
                Daily -> "daily"
                Infinity -> "infinity"
            }
        }

    val emoji: String
        get() {
            return when (this) {
                Daily -> "\uD83D\uDCC6"
                Infinity -> "♾\uFE0F"
            }
        }

    companion object {
        fun fromDb(dbName: String): GameMode {
            return GameMode.entries.first { it.dbName == dbName }
        }

        val NavType = object : NavType<GameMode>(isNullableAllowed = false) {
            override fun serializeAsValue(value: GameMode) = value.dbName
            override fun parseValue(value: String) = GameMode.fromDb(value)
            override fun get(bundle: Bundle, key: String) = bundle.getString(key)?.let { GameMode.fromDb(it) }
            override fun put(bundle: Bundle, key: String, value: GameMode) {
                bundle.putString(key, value.dbName)
            }
        }
    }
}