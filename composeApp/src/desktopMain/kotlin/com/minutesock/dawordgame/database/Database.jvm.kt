package com.minutesock.dawordgame.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "database.db")
    return Room.databaseBuilder(
        name = dbFile.absolutePath
    )
}