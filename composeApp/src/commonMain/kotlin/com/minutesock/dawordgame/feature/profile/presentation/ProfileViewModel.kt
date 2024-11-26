package com.minutesock.dawordgame.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.dawordgame.core.data.DataStoreManager
import com.minutesock.dawordgame.openWebsite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            DataStoreManager.dataStore.data.collect { prefs ->
                val darkMode = prefs[DataStoreManager.darkModeKey] ?: true
                val useSystemTheme = prefs[DataStoreManager.useSystemThemeKey] ?: false
                _state.update {
                    it.copy(
                        darkModeToggle = darkMode,
                        useSystemThemeToggle = useSystemTheme
                    )
                }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        viewModelScope.launch {
            when (event) {
                ProfileEvent.ClickWebsite -> openWebsite("https://www.minutesock.com/")
                ProfileEvent.DarkModeToggle -> DataStoreManager.darkMode = !DataStoreManager.darkMode
                ProfileEvent.UseSystemThemeToggle -> DataStoreManager.useSystemTheme = !DataStoreManager.useSystemTheme
            }
        }
    }
}