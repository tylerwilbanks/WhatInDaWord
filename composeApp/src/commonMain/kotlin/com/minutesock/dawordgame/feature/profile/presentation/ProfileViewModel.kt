package com.minutesock.dawordgame.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minutesock.dawordgame.core.data.DataStoreManager
import com.minutesock.dawordgame.feature.profile.presentation.ui.ProfilePreferenceState
import com.minutesock.dawordgame.openWebsite
import kotlinx.collections.immutable.persistentListOf
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
                val showLogo = prefs[DataStoreManager.showLogoKey] ?: true
                _state.update {
                    it.copy(
                        preferences = persistentListOf(
                            ProfilePreferenceState(
                                preference = ProfilePreference.UseSystemTheme,
                                value = useSystemTheme,
                                enabled = true
                            ),
                            ProfilePreferenceState(
                                preference = ProfilePreference.DarkMode,
                                value = darkMode,
                                enabled = !useSystemTheme
                            ),
                            ProfilePreferenceState(
                                preference = ProfilePreference.ShowLogo,
                                value = showLogo,
                                enabled = true
                            )
                        )
                    )
                }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        viewModelScope.launch {
            when (event) {
                ProfileEvent.ClickWebsite -> openWebsite("https://www.minutesock.com/")
                is ProfileEvent.PreferenceToggle -> {
                    when (event.preference) {
                        ProfilePreference.DarkMode -> DataStoreManager.darkMode = !DataStoreManager.darkMode
                        ProfilePreference.UseSystemTheme -> DataStoreManager.useSystemTheme =
                            !DataStoreManager.useSystemTheme

                        ProfilePreference.ShowLogo -> DataStoreManager.showLogo = !DataStoreManager.showLogo
                    }
                }
            }
        }
    }
}