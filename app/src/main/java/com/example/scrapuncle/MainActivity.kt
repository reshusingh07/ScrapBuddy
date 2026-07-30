package com.example.scrapuncle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.scrapuncle.auth.viewmodel.AuthViewModel
import com.example.scrapuncle.navigation.AppNavGraph
import com.example.scrapuncle.ui.theme.ScrapUncleTheme
import com.example.scrapuncle.ui.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val themeState by themeViewModel.uiState.collectAsState()

            // Draw nothing until the stored theme has been read, so the first painted
            // frame is always the correct theme instead of the default followed by a
            // switch. The near-black windowBackground covers this (sub-frame) gap.
            if (themeState.isLoaded) {
                ScrapUncleTheme(appTheme = themeState.theme) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = hiltViewModel()

                    AppNavGraph(
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}
