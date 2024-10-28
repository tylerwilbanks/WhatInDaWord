package com.minutesock.dawordgame.core.domain

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

    companion object {
        fun fromDb(dbName: String): GameMode {
            return GameMode.entries.first { it.dbName == dbName }
        }
    }
}