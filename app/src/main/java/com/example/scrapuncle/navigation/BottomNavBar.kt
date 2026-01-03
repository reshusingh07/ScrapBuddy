package com.example.scrapuncle.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun FloatingBottomNavBar(
    navController: NavHostController
) {
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    val shouldShow = currentRoute in bottomTabs.map { it.route }

    if (!shouldShow) return

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .padding(bottom = 14.dp) // floating gap
                .padding(horizontal = 32.dp)
                .padding(
                    bottom = WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding()
                )
                .height(52.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 12.dp,
            color = Color(0xFF1E1E1E) // dark floating bar
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                bottomTabs.forEach { tab ->
                    val selected = currentRoute == tab.route

                    FloatingNavItem(
                        selected = selected,
                        icon = if (selected) tab.selectedIcon else tab.unSelectedIcon,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun FloatingNavItem(
    selected: Boolean,
    icon: Int,
    onClick: () -> Unit
) {


    Box(
        modifier = Modifier
            .size(36.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = if (selected) Color.White else Color.Gray,
            modifier = Modifier.size(18.dp)
        )
    }
}


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
                selected = selected,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    if(selected)
                        Icon(
                            painter = painterResource(id = tab.selectedIcon),
                            modifier = Modifier.size(20.dp),
                            contentDescription = tab.label
                        )
                    else
                        Icon(
                            painter = painterResource(id = tab.unSelectedIcon),
                            modifier = Modifier.size(20.dp),
                            contentDescription = tab.label
                        )
                },
                label = {
                    Text(
                        text = tab.label,
                    )
                },
                alwaysShowLabel = true,

                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = Color(0xFF00A651),
//                    selectedTextColor = Color(0xFF00A651),
//                    unselectedIconColor = Color(0xFF64748B),
//                    unselectedTextColor = Color(0xFF64748B),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}