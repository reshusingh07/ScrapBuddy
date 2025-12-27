package com.example.scrapuncle.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.scrapuncle.AboutUsScreen
import com.example.scrapuncle.auth.ui.AccountSettingsRoute
import com.example.scrapuncle.auth.ui.AddAddressScreen
import com.example.scrapuncle.auth.ui.CreateProfileScreen
import com.example.scrapuncle.auth.ui.LoginScreen
import com.example.scrapuncle.auth.ui.OtpScreen
import com.example.scrapuncle.auth.ui.Splash
import com.example.scrapuncle.auth.ui.WelcomeScreen
import com.example.scrapuncle.auth.viewmodel.AuthViewModel
import com.example.scrapuncle.auth.viewmodel.ScheduleViewModel
import com.example.scrapuncle.pages.schedule.SchedulePickupScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 4 },
                animationSpec = tween(300)
            ) + fadeOut()
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 4 },
                animationSpec = tween(300)
            ) + fadeIn()
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut()
        }
    ) {

        // ---------------------------------------------------------
        // SPLASH
        // ---------------------------------------------------------
        composable(Screen.Splash.route) {
            Splash { loggedIn ->
                if (loggedIn) {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // AUTH FLOW
        // ---------------------------------------------------------
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToOtp = { navController.navigate(Screen.Otp.route) }
            )
        }

        composable(Screen.Otp.route) {
            OtpScreen(
                authViewModel = authViewModel,
                onNavigateToCreateProfile = {
                    navController.navigate(Screen.CreateProfile.route)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.CreateProfile.route) {
            CreateProfileScreen(
                onCreateAccount = {
                    navController.navigate(Screen.Splash.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // ---------------------------------------------------------
        // MAIN (BOTTOM NAV HOST)
        // ---------------------------------------------------------
        composable(Screen.Main.route) {
            MainScreen(rootNavController = navController)
        }

        // ---------------------------------------------------------
        // SCHEDULE FLOW (SHARED VIEWMODEL GRAPH) ⭐
        // ---------------------------------------------------------
        navigation(
            startDestination = Screen.SchedulePickup.route,
            route = Screen.ScheduleGraph.route
        ) {

            composable(Screen.SchedulePickup.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.ScheduleGraph.route)
                }

                val scheduleViewModel: ScheduleViewModel =
                    hiltViewModel(parentEntry)

                SchedulePickupScreen(
                    viewModel = scheduleViewModel,
                    onBack = { navController.popBackStack() },
                    onSelectAddress = {
                        navController.navigate(Screen.AddAddress.route)
                    }
                )
            }

            composable(Screen.AddAddress.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.ScheduleGraph.route)
                }

                val scheduleViewModel: ScheduleViewModel =
                    hiltViewModel(parentEntry)

                AddAddressScreen(
                    viewModel = hiltViewModel(), // AddAddressViewModel
                    scheduleViewModel = scheduleViewModel,
                    onSchedulePickup = {
                        navController.popBackStack()
                    }
                )
            }
        }

        // ---------------------------------------------------------
        // OTHER SCREENS
        // ---------------------------------------------------------
        composable(Screen.AccountSetting.route) {
            AccountSettingsRoute(
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() },
                onSignedOut = { navController.navigate(Screen.Welcome.route) }
            )
        }

        composable(Screen.AboutUs.route) {
            AboutUsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
