package com.example.scrapuncle.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.scrapuncle.R
import com.example.scrapuncle.auth.data.HorizontalCards
import com.example.scrapuncle.auth.skeleton.HomeSkeleton
import com.example.scrapuncle.compoents.BottomInfoSection
import com.example.scrapuncle.compoents.HorizontalCardSection
import com.example.scrapuncle.compoents.PickupButton
import kotlinx.coroutines.delay


@Composable
fun HomeScreen(
    onScheduleNow: () -> Unit
) {

    var showSkeleton by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1200)
        showSkeleton = false
    }

    if ( showSkeleton) {
        HomeSkeleton()
    } else {
        HomeContent(onScheduleNow)
    }



}

@Composable
fun HomeContent( onScheduleNow: () -> Unit) {
    val sampleCards = listOf(
        HorizontalCards("Schedule a pickup", R.drawable.card01),
        HorizontalCards("Pickup at your address", R.drawable.card02),
        HorizontalCards("Receive payment",  R.drawable.card03)
    )

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize(),
//        contentAlignment = Alignment.Center

    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth(0.36f)
                    .aspectRatio(1f)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )


            PickupButton(onClick = { onScheduleNow() })


            // Horizontal Card Section
            HorizontalCardSection(cards = sampleCards)

            // Bottom Section of Home Page
            BottomInfoSection()

        }


    }
}




//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun HomeScreenPreview() {
//    HomeScreen()
////    BottomInfoSection()
//}