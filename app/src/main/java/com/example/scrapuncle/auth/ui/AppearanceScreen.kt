package com.example.scrapuncle.auth.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.scrapuncle.R
import com.example.scrapuncle.data.theme.AppTheme
import com.example.scrapuncle.ui.theme.InterFontFamily
import com.example.scrapuncle.ui.theme.ModernGreenPrimary
import com.example.scrapuncle.ui.theme.ThemeViewModel

data class ThemeOption(
    val theme: AppTheme,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val isDefault: Boolean = false
)

@Composable
fun AppearanceRoute(
    onBack: () -> Unit,
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val themeState by themeViewModel.uiState.collectAsState()

    AppearanceScreen(
        selectedTheme = themeState.theme,
        onThemeSelected = { themeViewModel.setTheme(it) },
        onBack = onBack
    )
}

@Composable
fun AppearanceScreen(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onBack: () -> Unit
) {
    val themeOptions = remember {
        listOf(
            ThemeOption(
                theme = AppTheme.LIGHT,
                title = "Light",
                description = "Clean light theme with vibrant green touches (Default)",
                icon = Icons.Default.LightMode,
                isDefault = true
            ),
            ThemeOption(
                theme = AppTheme.DARK,
                title = "Dark",
                description = "Modern dark theme with pure black and green accents",
                icon = Icons.Default.DarkMode
            ),
            ThemeOption(
                theme = AppTheme.SYSTEM_DEFAULT,
                title = "System Default",
                description = "Automatically match your device's theme settings",
                icon = Icons.Default.SettingsSuggest
            )
        )
    }

    val backgroundColor = MaterialTheme.colorScheme.background
    val textColor = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onBack() },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_arrow_back),
                            contentDescription = "Back",
                            tint = textColor.copy(alpha = 0.85f),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    Text(
                        text = "Appearance",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        fontSize = 20.sp
                    )
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Theme Preference",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Select your preferred color mode for the ScrapUncle app.",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(20.dp))
            }

            items(themeOptions, key = { it.theme.name }) { option ->
                val isSelected = option.theme == selectedTheme

                ThemeSelectionCard(
                    option = option,
                    isSelected = isSelected,
                    onSelect = { onThemeSelected(option.theme) }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ThemeSelectionCard(
    option: ThemeOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val cardBgColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "cardBgColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            ModernGreenPrimary
        } else {
            MaterialTheme.colorScheme.outline
        },
        animationSpec = tween(durationMillis = 250),
        label = "borderColor"
    )

    val iconTint by animateColorAsState(
        targetValue = if (isSelected) ModernGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 200),
        label = "iconTint"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.01f else 1.0f,
        animationSpec = tween(durationMillis = 200),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .background(cardBgColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onSelect() }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) ModernGreenPrimary.copy(alpha = 0.15f)
                            else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                        )
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = option.title,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = option.title,
                            fontFamily = InterFontFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        if (option.isDefault) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ModernGreenPrimary.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "DEFAULT",
                                    fontFamily = InterFontFamily,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ModernGreenPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = option.description,
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = ModernGreenPrimary,
                    unselectedColor = MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}
