package com.minutesock.dawordgame.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val applicationContext = context.applicationContext
    val dbFile = applicationContext.getDatabasePath("database.db")
    return Room.databaseBuilder(
        context = applicationContext,
        name = dbFile.absolutePath
    )
}