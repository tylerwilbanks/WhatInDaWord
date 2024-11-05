package com.minutesock.dawordgame.core.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.minutesock.dawordgame.sqldelight.AppDatabase
import java.io.File

actual class ProductionDatabaseDriverFactory : DatabaseDriverFactory {
    actual override fun createDriver(): SqlDriver {
        val databaseDir = File(System.getProperty("user.home"), ".whatindaword")
        if (!databaseDir.exists()) {
            databaseDir.mkdirs()
        }
        val databaseFile = File(databaseDir, "app.db")
        val databaseShouldBeCreated = !databaseFile.exists()
        val driver = JdbcSqliteDriver("jdbc:sqlite:${databaseFile.absolutePath}")
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