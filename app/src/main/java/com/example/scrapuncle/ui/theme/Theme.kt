package com.example.scrapuncle.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsControllerCompat

private val DarkColorScheme = darkColorScheme(
    background = Color.Black,
    surface = Color.Black,
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)




private val LightColorScheme = lightColorScheme(
    background = Color.White,   // your app background
    surface = Color.White,
    surfaceTint = Color.Transparent
)

@Composable
fun ScrapUncleTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val activity = context as Activity

    SideEffect {
        val window = activity.window
        // Enable edge-to-edge
        window.setDecorFitsSystemWindows(false)

        // Transparent status bar
        window.statusBarColor = Color.Transparent.toArgb()

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = true // dark icons
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

//@Composable
//fun ScrapUncleTheme(content: @Composable () -> Unit) {
//
//    val darkTheme = isSystemInDarkTheme()
//    val context = LocalContext.current
//    val activity = context as Activity
//
//    val colorScheme = if (darkTheme) {
//        DarkColorScheme
//    } else {
//        LightColorScheme
//    }
//
//    SideEffect {
//        val window = activity.window
//
//        window.statusBarColor = if (darkTheme) {
//            Color.Black.toArgb()
//        } else {
//            Color.White.toArgb()
//        }
//
//        val controller = WindowInsetsControllerCompat(window, window.decorView)
//        controller.isAppearanceLightStatusBars = !darkTheme
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
//}
//
