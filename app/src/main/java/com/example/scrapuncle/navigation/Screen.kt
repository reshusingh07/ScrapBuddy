package com.example.scrapuncle.navigation


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
    val selectedIcon: Int,
    val unSelectedIcon: Int

) {

    object Home : BottomTab(
        route = Screen.Home.route,
        label = "Home",
        selectedIcon = R.drawable.icon_selected_house,
        unSelectedIcon = R.drawable.icon_unselected_house,
    )

    object Rate : BottomTab(
        route = Screen.Rate.route,
        label = "Rate",
        selectedIcon = R.drawable.icon_selected_tag,
        unSelectedIcon = R.drawable.icon_unselected_tag
    )

    object Schedule : BottomTab(
        route = Screen.Schedule.route,
        label = "Schedule",
        selectedIcon = R.drawable.icon_selected_pickups,
        unSelectedIcon = R.drawable.icon_unselected_pickups
    )

    object Profile : BottomTab(
        route = Screen.Profile.route,
        label = "Profile",
        selectedIcon = R.drawable.icon_selected_profile,
        unSelectedIcon = R.drawable.icon_unselected_profile
    )
}

val bottomTabs = listOf(
    BottomTab.Home,
    BottomTab.Rate,
    BottomTab.Schedule,
    BottomTab.Profile
)


