package com.example.scrapuncle.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen(rootNavController: NavHostController, startTab: String? = null) {

    val bottomNavController = rememberNavController()

    // Map the string tab back to a type-safe object
    val startDestination: Any = when (startTab) {
        Screen.Main.TAB_SCHEDULE -> Screen.Schedule
        Screen.Main.TAB_RATE -> Screen.Rate
        Screen.Main.TAB_PROFILE -> Screen.Profile
        else -> Screen.Home
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Main content
        MainNavGraph(
            navController = bottomNavController,
            rootNavController = rootNavController,
            startDestination = startDestination,
            modifier = Modifier.padding(bottom = 90.dp) // space for floating bar
        )

        // Floating Bottom Bar (OVERLAY)
        FloatingBottomNavBar(
            navController = bottomNavController
        )
    }

}

