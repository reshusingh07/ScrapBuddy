package com.example.scrapuncle.auth.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PickupDetailSkeleton() {
    val brush = rememberShimmerBrush()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(vertical = 18.dp, horizontal = 22.dp)
    ) {

        // ---------- TITLE ----------
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(24.dp)
                .background(brush, RoundedCornerShape(8.dp))
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(14.dp)
                .background(brush, RoundedCornerShape(6.dp))
                .align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(18.dp))

        // ---------- CARD ----------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {

            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(brush, RoundedCornerShape(20.dp))
            )

            Spacer(Modifier.height(14.dp))

            // Address
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .background(brush, RoundedCornerShape(6.dp))
            )

            Spacer(Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(14.dp)
                    .background(brush, RoundedCornerShape(6.dp))
            )

            Spacer(Modifier.height(14.dp))

            repeat(4) {
                InfoRowSkeleton(brush)
                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.weight(1f))

        // ---------- BUTTON ----------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(brush, RoundedCornerShape(24.dp))
        )
    }
}


@Composable
private fun InfoRowSkeleton(brush: Brush) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(12.dp)
                .background(brush, RoundedCornerShape(6.dp))
        )

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .height(12.dp)
                .background(brush, RoundedCornerShape(6.dp))
        )
    }
}
