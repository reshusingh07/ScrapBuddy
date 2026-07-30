package com.example.scrapuncle.ui.theme

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import com.example.scrapuncle.data.theme.AppTheme

private val DarkColorScheme = darkColorScheme(
    primary = ModernGreenPrimary,
    // White rather than black on green: the brand uses white labels on its green
    // buttons, and onPrimary is what Material's filled Button/FAB pick up by default.
    onPrimary = Color.White,
    primaryContainer = ModernGreenDark,
    onPrimaryContainer = Color.White,
    secondary = ModernGreenSecondary,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF1E3A27),
    onSecondaryContainer = ModernGreenSecondary,
    tertiary = ModernGreenDark,
    onTertiary = Color.White,
    tertiaryContainer = DarkNoticeContainer,
    onTertiaryContainer = DarkNoticeContent,
    background = PureBlackBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurfaceContainer,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkCardSurface,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorderColor,
    outlineVariant = Color(0xFF383838)
)

private val LightColorScheme = lightColorScheme(
    primary = Green80,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8F5E9),
    onPrimaryContainer = Green80,
    secondary = lightGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8F5E9),
    onSecondaryContainer = lightGreen,
    tertiary = ModernGreenDark,
    onTertiary = Color.White,
    tertiaryContainer = LightNoticeContainer,
    onTertiaryContainer = LightNoticeContent,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightCardSurface,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorderColor,
    outlineVariant = Color(0xFFD1D5DB)
)

@Composable
fun ScrapUncleTheme(
    appTheme: AppTheme = AppTheme.DARK,
    content: @Composable () -> Unit
) {
    val darkTheme = when (appTheme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.SYSTEM_DEFAULT -> isSystemInDarkTheme()
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode && view.context is Activity) {
        SideEffect {
            val window = (view.context as Activity).window
            window.setDecorFitsSystemWindows(false)
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()

            val controller = WindowInsetsControllerCompat(window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        // Single place that paints the app background. Without this, screens that do not
        // draw their own background fall through to the XML windowBackground and ignore
        // the selected theme. animateColorAsState makes switching themes a smooth fade
        // rather than an instant swap.
        val animatedBackground by animateColorAsState(
            targetValue = colorScheme.background,
            animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
            label = "appBackground"
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = animatedBackground,
            contentColor = colorScheme.onBackground,
            content = content
        )
    }
}
