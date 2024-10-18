package com.minutesock.dawordgame.core.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.minutesock.dawordgame.AndroidContextProvider
import com.minutesock.dawordgame.sqldelight.AppDatabase

actual class ProductionDatabaseDriverFactory(private val context: Context) : DatabaseDriverFactory {
    actual override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, "app.db")
    }
}

actual class TestDatabaseDriverFactory : DatabaseDriverFactory {
    actual override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            AppDatabase.Schema,
            AndroidContextProvider.applicationContext,
            "testapp.db"
        )
    }
}