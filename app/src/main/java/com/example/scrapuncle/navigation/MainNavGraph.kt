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


@Composable
fun MainNavGraph(
    navController: NavHostController,
    rootNavController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = Modifier.systemBarsPadding(),
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = hiltViewModel(),
                onNavigateToAccountSetting = {
                    rootNavController.navigate(Screen.AccountSetting.route)
                },
                onScheduleNow = {
                    rootNavController.navigate(Screen.SchedulePickup.route)
                }
            )
        }

        composable(Screen.Rate.route) {
            Column {
                RateHeader()
                RateScreen()
            }
        }

        composable(Screen.Schedule.route) {
            ScheduleScreen(
                onScheduleNow = {
                    rootNavController.navigate(Screen.SchedulePickup.route)
                },
                onPickupClick = { pid ->
                    rootNavController.navigate(
                        Screen.PickupDetails.createRoute(pid)
                    )
                }
            )
        }


        composable(Screen.Profile.route) {
            ProfileScreen(
                viewModel = hiltViewModel(),
                onNavigateToAccountSetting = {
                    rootNavController.navigate(Screen.AccountSetting.route)
                },
                onNavigateToAboutUs = {
                    rootNavController.navigate(Screen.AboutUs.route)
                }
            )
        }
    }
}
