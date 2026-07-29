package com.example.scrapuncle.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrapuncle.data.theme.AppTheme
import com.example.scrapuncle.data.theme.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Single source of truth for the selected [AppTheme].
 *
 * Owned by MainActivity so the whole Compose tree observes one instance; the Appearance
 * screen reads and writes through this same ViewModel rather than touching DataStore.
 */
@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themePreferences: ThemePreferences
) : ViewModel() {

    val uiState: StateFlow<ThemeUiState> = themePreferences.themeStream
        .map { theme -> ThemeUiState(theme = theme, isLoaded = true) }
        .stateIn(
            scope = viewModelScope,
            // Eagerly rather than WhileSubscribed: start reading DataStore as soon as the
            // ViewModel is created so the stored theme is ready by first composition.
            started = SharingStarted.Eagerly,
            initialValue = ThemeUiState()
        )

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            themePreferences.setTheme(theme)
        }
    }
}
