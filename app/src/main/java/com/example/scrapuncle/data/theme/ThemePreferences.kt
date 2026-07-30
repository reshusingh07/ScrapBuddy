package com.example.scrapuncle.data.theme

import kotlinx.coroutines.flow.Flow

interface ThemePreferences {
    val themeStream: Flow<AppTheme>
    suspend fun setTheme(theme: AppTheme)
}
