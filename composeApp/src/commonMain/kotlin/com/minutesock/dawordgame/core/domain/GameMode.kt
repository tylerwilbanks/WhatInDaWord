package com.minutesock.dawordgame.core.domain

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import whatindaword.composeapp.generated.resources.Res
import whatindaword.composeapp.generated.resources.calendar_today
import whatindaword.composeapp.generated.resources.infinity

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

    val icon: DrawableResource
        get() {
            return when (this) {
                Daily -> Res.drawable.calendar_today
                Infinity -> Res.drawable.infinity
            }
        }

    val explanationText: String
        get() {
            return when (this) {
                Daily -> "In daily mode, a mystery word is chosen each day (12 a.m. on your device). The mystery word is the same for everyone on the same date."
                Infinity -> "In infinity mode, you are no longer constrained to one new word per day. You may play as many times as you wish. After completing an infinity mode game, on the definition screen, tap the next button to play again."
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