package com.minutesock.dawordgame.core.data

import app.cash.sqldelight.db.SqlDriver
import com.minutesock.dawordgame.sqldelight.AppDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema, "app.db")
    }
}