package com.example.scrapuncle.navigation


import com.example.scrapuncle.R
import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    object Splash

    @Serializable
    object Welcome

    @Serializable
    object Login

    @Serializable
    object Otp

    @Serializable
    object CreateProfile

    @Serializable
    data class Main(val tab: String? = null) {
        companion object {
            const val TAB_HOME = "home"
            const val TAB_RATE = "rate"
            const val TAB_SCHEDULE = "schedule"
            const val TAB_PROFILE = "profile"
        }
    }

    // Bottom destinations
    @Serializable
    object Home

    @Serializable
    object Rate

    @Serializable
    object Schedule

    @Serializable
    object Profile

    // Other screens
    @Serializable
    object ScheduleGraph

    @Serializable
    object AddAddress

    @Serializable
    object SchedulePickup

    @Serializable
    object AccountSetting

    @Serializable
    object AboutUs

    @Serializable
    object RateHeader

    @Serializable
    data class PickupDetails(val pid: String)

}

sealed class BottomTab(
    val route: Any,
    val label: String,
    val selectedIcon: Int,
    val unSelectedIcon: Int
) {

    object Home : BottomTab(
        route = Screen.Home,
        label = "Home",
        selectedIcon = R.drawable.icon_selected_house,
        unSelectedIcon = R.drawable.icon_unselected_house,
    )

    object Rate : BottomTab(
        route = Screen.Rate,
        label = "Rate",
        selectedIcon = R.drawable.icon_selected_tag,
        unSelectedIcon = R.drawable.icon_unselected_tag
    )

    object Schedule : BottomTab(
        route = Screen.Schedule,
        label = "Schedule",
        selectedIcon = R.drawable.icon_selected_pickups,
        unSelectedIcon = R.drawable.icon_unselected_pickups
    )

    object Profile : BottomTab(
        route = Screen.Profile,
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


