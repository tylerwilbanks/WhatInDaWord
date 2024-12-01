package com.minutesock.dawordgame.core.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.minutesock.dawordgame.sqldelight.AppDatabase

actual class ProductionDatabaseDriverFactory : DatabaseDriverFactory {
    actual override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema, "app.db")
    }
}

actual class TestDatabaseDriverFactory : DatabaseDriverFactory {
    actual override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema, "testapp.db")
    }
}