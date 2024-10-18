package com.minutesock.dawordgame.core.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.minutesock.dawordgame.sqldelight.AppDatabase

actual class ProductionDatabaseDriverFactory : DatabaseDriverFactory {
    actual override fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
            AppDatabase.Schema.create(this@apply)
        }
    }
}

actual class TestDatabaseDriverFactory : DatabaseDriverFactory {
    actual override fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
            AppDatabase.Schema.create(this@apply)
        }
    }
}