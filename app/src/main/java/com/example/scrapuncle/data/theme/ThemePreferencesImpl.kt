package com.example.scrapuncle.data.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemePreferencesImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ThemePreferences {

    private object PreferencesKeys {
        val KEY_THEME = stringPreferencesKey("selected_app_theme")
    }

    override val themeStream: Flow<AppTheme> = dataStore.data.map { preferences ->
        val savedTheme = preferences[PreferencesKeys.KEY_THEME] ?: AppTheme.LIGHT.name
        try {
            AppTheme.valueOf(savedTheme)
        } catch (e: Exception) {
            AppTheme.LIGHT
        }
    }

    override suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_THEME] = theme.name
        }
    }
}
