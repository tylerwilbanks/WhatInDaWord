package com.minutesock.dawordgame.core.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.minutesock.dawordgame.core.data.DataStoreManager.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okio.Path.Companion.toPath
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface DataStoreProvider {
    val dataStore: DataStore<Preferences>
}

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

internal const val dataStoreFileName = "whatindaword.preferences_pb"

class DataStoreDelegate<T>(
    val key: Preferences.Key<T>,
    val defaultValue: T
) : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return runBlocking {
            dataStore.data.map { preferences ->
                preferences[key] ?: defaultValue
            }.first()
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        runBlocking {
            dataStore.edit { preferences ->
                preferences[key] = value
            }
        }
    }
}

@Composable
fun <T> rememberPreference(
    dataStoreDelegate: DataStoreDelegate<T>,
    defaultOverride: T? = null
): State<T> {
    return dataStore
        .data
        .map { preferences: Preferences ->
            preferences[dataStoreDelegate.key] ?: defaultOverride ?: dataStoreDelegate.defaultValue
        }
        .collectAsStateWithLifecycle(defaultOverride ?: dataStoreDelegate.defaultValue)
}

object DataStoreManager : KoinComponent {
    val dataStore: DataStore<Preferences> = inject<DataStoreProvider>().value.dataStore
    val darkModeKey = booleanPreferencesKey("dark_mode_enabled")
    val useSystemThemeKey = booleanPreferencesKey("use_system_theme")
    val showLogoKey = booleanPreferencesKey("show_logo")

    val darkModeDelegate = DataStoreDelegate(
        key = darkModeKey,
        defaultValue = true
    )
    var darkMode by darkModeDelegate

    val useSystemThemeDelegate = DataStoreDelegate(
        key = useSystemThemeKey,
        defaultValue = false
    )
    var useSystemTheme by useSystemThemeDelegate

    val showLogoDelegate = DataStoreDelegate(
        key = showLogoKey,
        defaultValue = true
    )
    var showLogo by showLogoDelegate
}



