package com.example.scrapuncle.navigation

import com.example.scrapuncle.R

sealed class Screen(
    val route: String,
    val label: String = "",
    val iconResId: Int? = null
) {


    // --- Splash / Auth ---
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Otp : Screen("otp")
    object CreateProfile : Screen("create_profile")

    // --- Main root ---
    object Main : Screen("main")

    // --- Bottom Nav Tabs ---
    object Home : Screen("home", "Home", R.drawable.icon_home)
    object Rate : Screen("rate", "Rate", R.drawable.icon_price_tag)
    object Schedule : Screen("schedule", "Schedule", R.drawable.icon_pickup)
    object Profile : Screen("profile", "Profile", R.drawable.icon_user)

    // --- Additional Screens ---
    object AddAddress : Screen("add_address")
    object SchedulePickup : Screen("schedule_pickup")
    object AccountSetting : Screen("account_setting")
    object AboutUs : Screen("about_us")


    // --- Schedule Flow Graph (parent) ---
    object ScheduleGraph : Screen("schedule_graph")

}


