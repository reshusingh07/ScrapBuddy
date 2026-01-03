package com.example.scrapuncle.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.scrapuncle.AboutUsScreen
import com.example.scrapuncle.PickupDetailScreen
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
import com.example.scrapuncle.pages.schedule.formatAddress
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
                initialOffsetX = { it / 1 },
                animationSpec = tween(
                    durationMillis = 330,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(
                animationSpec = tween(180)
            )
        },

        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 2 },
                animationSpec = tween(
                    durationMillis = 260,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(
                animationSpec = tween(160)
            )
        },

        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 2 },
                animationSpec = tween(
                    durationMillis = 280,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(
                animationSpec = tween(160)
            )
        },

        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it / 1 },
                animationSpec = tween(
                    durationMillis = 260,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(
                animationSpec = tween(140)
            )
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
        // SCHEDULE FLOW (SHARED VIEWMODEL GRAPH)
        // ---------------------------------------------------------
        navigation(
            startDestination = Screen.SchedulePickup.route,
            route = Screen.ScheduleGraph.route
        ) {

            composable(Screen.SchedulePickup.route) { backStackEntry ->


                SchedulePickupScreen(
                    viewModel = hiltViewModel(),
                    onBack = { navController.popBackStack() },
                    onSelectAddress = {
                        navController.navigate(Screen.AddAddress.route)
                    }
                )
            }

            composable(Screen.AddAddress.route) { backStackEntry ->


                AddAddressScreen(
                    viewModel = hiltViewModel(), // AddAddressViewModel
                    scheduleViewModel = hiltViewModel(),
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
                onSignedOut = {
                    navController.navigate(Screen.Splash.route) {
                        popUpTo(0) { inclusive = true } // clears entire back stack
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.AboutUs.route) {
            AboutUsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.PickupDetails.route) { backStackEntry ->

            val pid = backStackEntry.arguments?.getString("pid") ?: return@composable
            PickupDetailScreen(
                pid = pid,
                onBack = { navController.popBackStack() }
            )
        }


    }
}
