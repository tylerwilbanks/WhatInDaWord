package com.minutesock.dawordgame.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createDataStore(context: Context): DataStore<Preferences> = createDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
)

class AndroidDataStoreProvider(context: Context) : DataStoreProvider {
    override val dataStore: DataStore<Preferences> = createDataStore(context)
}