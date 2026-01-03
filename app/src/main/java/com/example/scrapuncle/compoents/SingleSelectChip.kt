package com.example.scrapuncle.compoents

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SingleSelectChip(
    label: String, selected: Boolean, onClick: () -> Unit
) {

    val scale by animateFloatAsState(
        targetValue = if (selected) 1.055f else 1.01f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "chipScale"
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .background(
                brush = if (selected)
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF00A651).copy(alpha = 0.75f),
                            Color(0xFF00A651).copy(alpha = 0.9f) // solid color as gradient
                        )
                    )
                else
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.1f),
                            Color.LightGray.copy(alpha = 0.14f)
                        )
                    ),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = Color.LightGray.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onClick()
            }
            .padding(horizontal = 12.dp, vertical = 6.dp)) {
        Text(
            text = label,
            color = if (selected) Color.White else Color.DarkGray.copy(alpha = 9f),
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp
        )
    }
}