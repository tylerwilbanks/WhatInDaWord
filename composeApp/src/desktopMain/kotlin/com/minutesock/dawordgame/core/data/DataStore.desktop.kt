package com.minutesock.dawordgame.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

fun createDataStore(): DataStore<Preferences> {
    val databaseDirectory = File(System.getProperty("user.home"), ".whatindaword")
    return createDataStore(
        producePath = { File(databaseDirectory, dataStoreFileName).absolutePath }
    )
}

class DesktopDataStoreProvider : DataStoreProvider {
    override val dataStore: DataStore<Preferences> = createDataStore()
}