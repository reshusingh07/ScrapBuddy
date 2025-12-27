package com.example.scrapuncle.compoents

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignOutConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    // Animation states
    val visible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible.value = true
    }



    // Fade background animation
    val bgAlpha by animateFloatAsState(
        targetValue = if (visible.value) 0.55f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    )

    // Slide & scale dialog animation
    val scale by animateFloatAsState(
        targetValue = if (visible.value) 1f else 0.6f,
        animationSpec = keyframes {
            durationMillis = 420

            0.6f at 0            // start small
            1.10f at 160         // overshoot peak
            0.97f at 260         // settle back
            1.0f at 420          // final perfect scale
        },
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = bgAlpha))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                visible.value = false
                onDismiss()
            },
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .padding(horizontal = 46.dp)
                .scale(scale)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(vertical = 24.dp, horizontal = 20.dp)

        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Title
                Text(
                    text = "Sign Out",
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = Color.Black.copy(alpha = 0.75f)
                )

                Spacer(Modifier.height(8.dp))

                // Message
                Text(
                    text = "Are you sure you want to sign out?",
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.7f)
                )

                Spacer(Modifier.height(26.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // Cancel Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF3F3F3).copy(alpha = 0.82f))
                            .clickable {
                                visible.value = false
                                onDismiss()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    // Sign Out Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF00A651).copy(alpha = 0.85f))
                            .clickable {
                                visible.value = false
                                onConfirm()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sign Out",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
