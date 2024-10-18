package com.minutesock.dawordgame.core.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.minutesock.dawordgame.sqldelight.AppDatabase
import java.io.File

actual class ProductionDatabaseDriverFactory : DatabaseDriverFactory {
    actual override fun createDriver(): SqlDriver {
        val databaseDir = File(System.getProperty("user.home"), ".whatindaword")
        val databaseShouldBeCreated = !databaseDir.exists()
        if (databaseShouldBeCreated) {
            databaseDir.mkdirs()
        }
        val databasePath = File(databaseDir, "app.db").absolutePath
        val driver = JdbcSqliteDriver("jdbc:sqlite:$databasePath")
        if (databaseShouldBeCreated) {
            AppDatabase.Schema.create(driver)
        }
        return driver
    }
}

actual class TestDatabaseDriverFactory : DatabaseDriverFactory {
    actual override fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
            AppDatabase.Schema.create(this@apply)
        }
    }
}