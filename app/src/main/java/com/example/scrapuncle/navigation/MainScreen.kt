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
fun MainScreen(rootNavController: NavHostController) {

    val bottomNavController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize()) {

        // Main content
        MainNavGraph(
            navController = bottomNavController,
            rootNavController = rootNavController,
            modifier = Modifier.padding(bottom = 90.dp) // space for floating bar
        )

        // Floating Bottom Bar (OVERLAY)
        FloatingBottomNavBar(
            navController = bottomNavController
        )
    }

}

