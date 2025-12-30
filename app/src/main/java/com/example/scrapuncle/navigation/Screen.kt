package com.example.scrapuncle.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.scrapuncle.R

sealed class Screen(val route: String) {

    // Auth / Flow
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Otp : Screen("otp")
    object CreateProfile : Screen("create_profile")

    // Main
    object Main : Screen("main")

    // Bottom destinations
    object Home : Screen("home")
    object Rate : Screen("rate")
    object Schedule : Screen("schedule")
    object Profile : Screen("profile")

    // Other screens
    object ScheduleGraph : Screen("schedule_graph")
    object AddAddress : Screen("add_address")
    object SchedulePickup : Screen("schedule_pickup")
    object AccountSetting : Screen("account_setting")
    object AboutUs : Screen("about_us")
    object RateHeader : Screen("rate_header")

    object PickupDetails : Screen("pickup_details/{pid}") {
        fun createRoute(pid: String): String {
            return "pickup_details/$pid"
        }
    }

}


sealed class BottomTab(
    val route: String,
    val label: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
) {

    object Home : BottomTab(
        route = Screen.Home.route,
        label = "Home",
        filledIcon = Icons.Filled.Home,
        outlinedIcon = Icons.Outlined.Home
    )

    object Rate : BottomTab(
        route = Screen.Rate.route,
        label = "Rate",
        filledIcon = Icons.Filled.Star,
        outlinedIcon = Icons.Outlined.StarBorder
    )

    object Schedule : BottomTab(
        route = Screen.Schedule.route,
        label = "Schedule",
        filledIcon = Icons.Filled.Event,
        outlinedIcon = Icons.Outlined.Event
    )

    object Profile : BottomTab(
        route = Screen.Profile.route,
        label = "Profile",
        filledIcon = Icons.Filled.Person,
        outlinedIcon = Icons.Outlined.Person
    )
}

val bottomTabs = listOf(
    BottomTab.Home,
    BottomTab.Rate,
    BottomTab.Schedule,
    BottomTab.Profile
)


