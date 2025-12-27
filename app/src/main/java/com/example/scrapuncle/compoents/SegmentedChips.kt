package com.example.scrapuncle.compoents

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrapuncle.auth.ui.Gender


@Composable
fun SegmentedChips(
    selected: Gender?,
    onSelected: (Gender) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = Gender.entries
    val selectedIndex = items.indexOf(selected)



    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE5E5E5).copy(alpha = 0.72f))
            .padding(4.dp)
    ) {
        val segmentWidth = maxWidth / items.size

        val indicatorOffset by animateDpAsState(
            targetValue = segmentWidth * selectedIndex,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "indicatorOffset"
        )

        // 🔹 Sliding indicator
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(segmentWidth)
                .fillMaxHeight()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
        )

        // 🔹 Labels
        Row(modifier = Modifier.fillMaxSize()) {
            items.forEach { gender ->
                val isSelected = gender == selected

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onSelected(gender) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = gender.label,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected) Color.Black else Color.DarkGray
                    )
                }
            }
        }
    }
}