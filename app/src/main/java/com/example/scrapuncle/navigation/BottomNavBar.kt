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

val bottomTabs = listOf(
    Screen.Home,
    Screen.Rate,
    Screen.Schedule,
    Screen.Profile
)

@Composable
fun BottomNavBar(
    navController: NavHostController
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        bottomTabs.forEach { screen ->

            val selected = currentRoute == screen.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {

                        // 🔥 THIS IS CRITICAL
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(screen.iconResId!!),
                        contentDescription = screen.label,
                        modifier = Modifier
                            .size(20.dp),
                        tint = if (selected)
                            Color(0xFF00A651)
                        else
                            Color.Black.copy(alpha = 0.7f)
                    )
                },
                label = {
                    Text(
                        text = screen.label,
                        fontSize = 10.sp,
                        color = if (selected)
                            Color(0xFF00A651)
                        else
                            Color(0xFF94A3B8)
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
