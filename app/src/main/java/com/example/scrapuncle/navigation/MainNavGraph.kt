package com.example.scrapuncle.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrapuncle.PickupDetailScreen
import com.example.scrapuncle.auth.viewmodel.ScheduleViewModel
import com.example.scrapuncle.pages.HomeScreen
import com.example.scrapuncle.pages.ProfileScreen
import com.example.scrapuncle.pages.RateHeader
import com.example.scrapuncle.pages.RateScreen
import com.example.scrapuncle.pages.schedule.ScheduleScreen
import com.example.scrapuncle.pages.schedule.formatAddress


import androidx.navigation.toRoute

@Composable
fun MainNavGraph(
    navController: NavHostController,
    rootNavController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Any = Screen.Home,
) {
    NavHost(
        modifier = modifier.systemBarsPadding(),
        navController = navController,
        startDestination = startDestination
    ) {

        composable<Screen.Home> {
            HomeScreen(
                viewModel = hiltViewModel(),
                onNavigateToAccountSetting = {
                    rootNavController.navigate(Screen.AccountSetting)
                },
                onScheduleNow = {
                    rootNavController.navigate(Screen.ScheduleGraph)
                }
            )
        }

        composable<Screen.Rate> {
            Column {
                RateHeader()
                RateScreen()
            }
        }

        composable<Screen.Schedule> {
            ScheduleScreen(
                onScheduleNow = {
                    rootNavController.navigate(Screen.ScheduleGraph)
                },
                onPickupClick = { pid ->
                    rootNavController.navigate(Screen.PickupDetails(pid))
                }
            )
        }


        composable<Screen.Profile> {
            ProfileScreen(
                viewModel = hiltViewModel(),
                onNavigateToAccountSetting = {
                    rootNavController.navigate(Screen.AccountSetting)
                },
                onNavigateToAboutUs = {
                    rootNavController.navigate(Screen.AboutUs)
                }
            )
        }
    }
}
