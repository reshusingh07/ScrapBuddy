package com.example.scrapuncle.auth.skeleton

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ShimmerPickupCardItem(
    brush: Brush
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

            /* ---------- DATE + STATUS ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(16.dp)
                        .background(brush, RoundedCornerShape(6.dp))
                )

                Spacer(Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(24.dp)
                        .background(brush, RoundedCornerShape(12.dp))
                )
            }

            /* ---------- ADDRESS (2 lines) ---------- */
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(14.dp)
                        .background(brush, RoundedCornerShape(6.dp))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(14.dp)
                        .background(brush, RoundedCornerShape(6.dp))
                )
            }

            /* ---------- SLOT ---------- */
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(14.dp)
                        .height(14.dp)
                        .background(brush, RoundedCornerShape(4.dp))
                )

                Spacer(Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(12.dp)
                        .background(brush, RoundedCornerShape(6.dp))
                )
            }

            /* ---------- PID ---------- */
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(10.dp)
                    .background(brush, RoundedCornerShape(6.dp))
            )
        }
    }
}


//@Composable
//fun rememberShimmerBrush(): Brush {
//    val shimmerColors = listOf(
//        Color(0xFFEDEDED),
//        Color(0xFFF5F5F5),
//        Color(0xFFEDEDED)
//    )
//
//    val transition = rememberInfiniteTransition(label = "shimmer")
//    val translate by transition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1000f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(
//                durationMillis = 1000,
//                easing = LinearEasing
//            ),
//            repeatMode = RepeatMode.Restart
//        ),
//        label = "translate"
//    )
//
//    return Brush.linearGradient(
//        colors = shimmerColors,
//        start = Offset(translate - 200f, 0f),
//        end = Offset(translate, 400f)
//    )
//}
//


//@Preview(showBackground = true)
//@Composable
//fun PreviewShimmerPickupCardItem() {
//    val shimmerColors = listOf(
//        Color.LightGray.copy(alpha = 0.6f),
//        Color.LightGray.copy(alpha = 0.25f),
//        Color.LightGray.copy(alpha = 0.6f)
//    )
//
//    val brush = Brush.linearGradient(
//        colors = shimmerColors,
//        start = Offset.Zero,
//        end = Offset(200f, 200f)
//    )
//
//    ShimmerPickupCardItem(brush = brush)
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewShimmerPickupList() {
//    ShimmerPickupList(count = 4)
//}
