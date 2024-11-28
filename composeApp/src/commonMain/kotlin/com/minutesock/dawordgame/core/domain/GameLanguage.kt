package com.minutesock.dawordgame.core.domain

import androidx.core.bundle.Bundle
import androidx.navigation.NavType

enum class GameLanguage {
    English,
    German,
    Spanish;

    val dbName: String get() {
        return when (this) {
            English -> "en"
            German -> "de"
            Spanish -> "es"
        }
    }

    val expectedValidWordCount: Long get() {
        return when (this) {
            English -> 12_484
            German -> 0
            Spanish -> 0
        }
    }

    val expectedWordSelectionCount: Long
        get() {
        return when (this) {
            English -> 2_315
            German -> 0
            Spanish -> 0
        }
    }

    val validWordFileName: String get() {
        return when (this) {
            English -> "valid_words.json"
            German -> TODO()
            Spanish -> TODO()
        }
    }

    val wordSelectionFileName: String get() {
        return when (this) {
            English -> "word_selection.json"
            German -> TODO()
            Spanish -> TODO()
        }
    }

    val idStartOffset: Long
        get() {
            return when (this) {
                English -> 1
                German -> English.expectedWordSelectionCount + 1
                Spanish -> English.expectedWordSelectionCount + German.expectedWordSelectionCount + 2
            }
        }

    companion object {
        fun fromDb(dbName: String): GameLanguage {
           return GameLanguage.entries.first { it.dbName == dbName }
        }

        fun fromSystem(languageName: String): GameLanguage {
            return GameLanguage.entries.firstOrNull { it.dbName.lowercase() == languageName.lowercase() }
                ?: English
        }

        val NavType = object : NavType<GameLanguage>(isNullableAllowed = false) {
            override fun serializeAsValue(value: GameLanguage) = value.dbName
            override fun parseValue(value: String) = GameLanguage.fromDb(value)
            override fun get(bundle: Bundle, key: String) = bundle.getString(key)?.let { GameLanguage.fromDb(it) }
            override fun put(bundle: Bundle, key: String, value: GameLanguage) {
                bundle.putString(key, value.dbName)
            }
        }
    }
}