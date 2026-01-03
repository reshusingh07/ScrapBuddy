package com.example.scrapuncle.auth.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeSkeleton() {
    val brush = rememberShimmerBrush()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(top = 10.dp, bottom = 12.dp)
    ) {

        // ---------------- HEADER SKELETON ----------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Profile image
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(brush, CircleShape)
            )

            Spacer(Modifier.width(12.dp))

            // Hello + username
            Column {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(10.dp)
                        .background(brush, RoundedCornerShape(4.dp))
                )

                Spacer(Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(14.dp)
                        .background(brush, RoundedCornerShape(6.dp))
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Notification bell
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(brush, CircleShape)
            )
        }

        Spacer(Modifier.height(20.dp))

        // ---------------- PRIMARY BUTTON ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 22.dp)
                .background(brush, RoundedCornerShape(14.dp))
        )

        Spacer(Modifier.height(28.dp))

        // ---------------- HORIZONTAL CARD ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .height(130.dp)
                .background(brush, RoundedCornerShape(20.dp))
        )

        Spacer(Modifier.height(10.dp))

        // Pager dots
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .width(6.dp)
                        .height(6.dp)
                        .background(brush, CircleShape)
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        // ---------------- BOTTOM INFO ----------------
        BottomInfoSkeleton(brush)
    }
}


@Composable
private fun HomeHorizontalCardSkeleton(brush: Brush) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .height(100.dp)
            .background(brush, RoundedCornerShape(20.dp))
    )
}

@Composable
private fun BottomInfoSkeleton(brush: Brush) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Info banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(brush, RoundedCornerShape(12.dp))
        )

        Spacer(Modifier.height(16.dp))

        // Big number
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(16.dp)
                .background(brush, RoundedCornerShape(6.dp))
        )

        Spacer(Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .height(12.dp)
                .background(brush, RoundedCornerShape(6.dp))
        )

        Spacer(Modifier.height(24.dp))

        // Banner image placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(brush, RoundedCornerShape(16.dp))
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewShimmerPickupList() {
  HomeSkeleton()
}
