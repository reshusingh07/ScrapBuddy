package com.example.scrapuncle.compoents

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrapuncle.R
import com.example.scrapuncle.auth.data.HorizontalCards
import com.example.scrapuncle.ui.theme.InterFontFamily
 import com.example.scrapuncle.ui.theme.lightGreen
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalCardSection(
    cards: List<HorizontalCards>
) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { cards.size }
    )

    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll every 3 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(3500)

            if (!pagerState.isScrollInProgress) {
                val nextPage = (pagerState.currentPage + 1) % cards.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    val greenGlowBrush = Brush.radialGradient(
        colors = listOf(
            Color(0xFF00A651).copy(alpha = 0.1f), // stronger green core
            Color(0x3FDFF5E4), // soft mid fade
            Color(0xFF00A651).copy(alpha = 0.1f) // off-white surface (NOT pure white)
        ),
        center = Offset(0f, 120f),
        radius = 550f
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 18.dp),
            pageSpacing = 16.dp,
            modifier = Modifier.fillMaxWidth()

        ) { page ->

            val card = cards[page]
            val scale by animateFloatAsState(
                if (pagerState.currentPage == page) 1f else 0.96f
            )


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(scale)
                    .height(130.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF48B758))
                        .background(greenGlowBrush)
                        .padding(12.dp),

                    ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // LEFT IMAGE
                        Image(
                            painter = painterResource(card.imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        // RIGHT TEXT
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = card.title,
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(alpha = 0.75f)
                            )


                            Text(
                                text = "Fast • Reliable • Doorstep",
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(4.dp))

        PagerIndicator(
            totalDots = cards.size,
            selectedIndex = pagerState.currentPage
        )
    }
}

@Composable
fun PagerIndicator(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            val isSelected = index == selectedIndex

            val width by animateDpAsState(
                if (isSelected) 18.dp else 6.dp,
                label = ""
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(6.dp)
                    .width(width)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (isSelected)
                            Color.DarkGray.copy(alpha = 0.8f)   // active green
                        else
                            Color(0xFFBDBDBD)   // inactive gray
                    )
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true )
@Composable
fun HorizontalCardSectionPreview() {
    val sampleCards = listOf(
        HorizontalCards("Pickup at your address",
            imageRes = R.drawable.card01),
        HorizontalCards("Hello", imageRes = R.drawable.card02),
        HorizontalCards("Hello", imageRes = R.drawable.card03)
    )

    HorizontalCardSection(cards = sampleCards)
}