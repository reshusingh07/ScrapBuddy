package com.example.scrapuncle.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomNavBar(
    navController: NavHostController
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White, tonalElevation = 0.dp
    ) {
        bottomTabs.forEach { tab ->

            val selected = currentRoute == tab.route

            NavigationBarItem(
                selected = selected, onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) tab.filledIcon
                        else tab.outlinedIcon, contentDescription = tab.label
                    )
                },
                label = {
                    Text(
                        text = tab.label,
                    )
                },
                alwaysShowLabel = true, colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF00A651),
                    selectedTextColor = Color(0xFF00A651),
                    unselectedIconColor = Color(0xFF64748B),
                    unselectedTextColor = Color(0xFF64748B),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
