package com.example.scrapuncle.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen(rootNavController: NavHostController) {

    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = bottomNavController
            )
        }
    ) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding)) {
            MainNavGraph(
                navController = bottomNavController,
                rootNavController = rootNavController
            )
        }
    }
}

