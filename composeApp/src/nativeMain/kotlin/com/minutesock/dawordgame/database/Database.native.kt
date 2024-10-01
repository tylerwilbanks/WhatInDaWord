package com.minutesock.dawordgame.database

import androidx.room.Room
import androidx.room.RoomDatabase

private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = null,
        error = null
    )
    return requireNotNull(documentDirectory?.path)
}

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = documentDirectory() + "database.db"
    return Room.databaseBuilder(
        name = dbFilePath
    )
}