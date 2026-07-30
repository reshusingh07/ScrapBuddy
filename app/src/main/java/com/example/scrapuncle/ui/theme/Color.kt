package com.example.scrapuncle.ui.theme

import androidx.compose.ui.graphics.Color

// Modern Green Accents
val ModernGreenPrimary = Color(0xFF00C853)
val ModernGreenSecondary = Color(0xFF22C55E)
val ModernGreenDark = Color(0xFF16A34A)
val Green80 = Color(0xFF00A651)
val lightGreen = Color(0xFF3AB34A)

// Premium Dark Theme Palette
val PureBlackBackground = Color(0xFF0A0A0A)
val DarkSecondaryBackground = Color(0xFF121212)
val DarkSurfaceContainer = Color(0xFF1A1A1A)
val DarkCardSurface = Color(0xFF222222)
val DarkBorderColor = Color(0xFF2E2E2E)

val DarkTextPrimary = Color(0xFFF8F9FA)
val DarkTextSecondary = Color(0xFFA0A5AA)
val DarkTextMuted = Color(0xFF6C7278)

// Pickup status colors. Deliberately shared by both themes: status is semantic, and these
// hues stay legible on near-black and on white. Pending used to be a bright blue
// (#1565C0), replaced with amber to keep the palette green-accented.
val StatusCompleted = Color(0xFF22C55E)
val StatusCancelled = Color(0xFFEF4444)
val StatusPending = Color(0xFFF59E0B)

// Informational notice banner ("We are currently operational in ...").
// Exposed through the scheme as tertiaryContainer/onTertiaryContainer so screens do not
// need to know which theme is active.
val DarkNoticeContainer = Color(0xFF2B2B1A)
val DarkNoticeContent = Color(0xFFE3E3B0)
val LightNoticeContainer = Color(0xFFE5E5A6)
val LightNoticeContent = Color(0xFF333322)

// Light Theme Palette
val LightBackground = Color(0xFFFFFFFF)
val LightSurface = Color(0xFFF8F9FA)
val LightCardSurface = Color(0xFFFFFFFF)
val LightBorderColor = Color(0xFFE5E7EB)

val LightTextPrimary = Color(0xFF111827)
val LightTextSecondary = Color(0xFF4B5563)
val LightTextMuted = Color(0xFF9CA3AF)

// Legacy Colors
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val lightBlack = Color(0xFF5D5C5C)
val lightWhite = Color(0xFFE5DFDF)
