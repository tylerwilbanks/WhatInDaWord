package com.minutesock.dawordgame.core.domain

enum class GameLanguage {
    English,
    German,
    Spanish;

    val dbName: String get() {
        return when (this) {
            English -> "EN"
            German -> "DE"
            Spanish -> "ES"
        }
    }

    val expectedValidWordCount: Int get() {
        return when (this) {
            English -> 12_484
            German -> 0
            Spanish -> 0
        }
    }

    val expectedWordSelectionCount: Int get() {
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

    companion object {
        fun fromDb(dbName: String): GameLanguage {
           return GameLanguage.entries.first { it.dbName == dbName }
        }
    }
}