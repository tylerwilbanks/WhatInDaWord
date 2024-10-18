package com.minutesock.dawordgame.core.data

import app.cash.sqldelight.db.SqlDriver

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

expect class ProductionDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver
}

expect class TestDatabaseDriverFactory() : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver
}

fun SqlDriver.performMigrations() {
    // todo-tyler handle migrations
}