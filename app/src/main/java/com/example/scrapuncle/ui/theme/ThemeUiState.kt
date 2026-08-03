package com.example.scrapuncle.ui.theme

import com.example.scrapuncle.data.theme.AppTheme

/**
 * State of the app-wide theme.
 *
 * [isLoaded] exists purely to prevent a startup flicker: DataStore's first emission is
 * asynchronous, so without it the UI would paint the default (Dark) for a frame before
 * switching to whatever the user actually picked. MainActivity draws nothing until this
 * turns true.
 */
data class ThemeUiState(
    val theme: AppTheme = AppTheme.LIGHT,
    val isLoaded: Boolean = false
)
