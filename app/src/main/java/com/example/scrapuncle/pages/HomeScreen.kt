package com.example.scrapuncle.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.scrapuncle.R
import com.example.scrapuncle.auth.data.HorizontalCards
import com.example.scrapuncle.auth.skeleton.HomeSkeleton
import com.example.scrapuncle.auth.viewmodel.AccountSettingsViewModel
import com.example.scrapuncle.compoents.BottomInfoSection
import com.example.scrapuncle.compoents.HorizontalCardSection
import com.example.scrapuncle.compoents.PickupButton
import com.example.scrapuncle.ui.theme.InterFontFamily
import kotlinx.coroutines.delay


@Composable
fun HomeScreen(
    viewModel: AccountSettingsViewModel = hiltViewModel(),
    onNavigateToAccountSetting: () -> Unit,
    onScheduleNow: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSkeleton by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1200)
        showSkeleton = false
    }

    if (showSkeleton) {
        HomeSkeleton()
    } else {
        HomeContent(
            uiState.fullName,
            uiState.gender,
            onNavigateToAccountSetting,
            onScheduleNow
        )
    }
}

@Composable
fun HomeContent(
    userName: String,
    gender: String,
    onNavigateToAccountSetting: () -> Unit,
    onScheduleNow: () -> Unit
) {
    val sampleCards = listOf(
        HorizontalCards("Schedule a pickup",R.drawable.card01),
        HorizontalCards("Pickup at your address", R.drawable.card02),
        HorizontalCards("Receive payment", R.drawable.card03)
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        // 🔥 HEADER
        HomeHeader(
            userName = userName,
            gender = gender,
            onNavigateToAccountSetting
        )

        // Optional divider spacing
        Spacer(modifier = Modifier.height(8.dp))


        Spacer(modifier = Modifier.height(12.dp))

        PickupButton(onClick = onScheduleNow)

        HorizontalCardSection(cards = sampleCards)

        BottomInfoSection()
    }
}


@Composable
fun HomeHeader(
    userName: String,
    gender: String,
    onNavigateToAccountSetting: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            shape = CircleShape,
            onClick = onNavigateToAccountSetting
        ) {
            Image(
                painter = painterResource(
                    if (gender == "MALE")
                        R.drawable.male_profile_img
                    else
                        R.drawable.female_profile_img

                ),
                contentDescription = "Profile",
                modifier = Modifier.size(46.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = "Hello 👋",
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                color = Color.DarkGray.copy(alpha = 0.82f)
            )
            Text(
                text = userName,
                fontFamily = InterFontFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(alpha = 0.84f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))


        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.12f))
                .clickable {},
            contentAlignment = Alignment.Center
        ) {
            // Notification Bell
            Icon(
                painter = painterResource(id = R.drawable.icon_notification),
                contentDescription = "Notifications",
                modifier = Modifier
                    .size(20.dp)
            )
        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun HomeScreenPreview() {
//    HomeScreen()
////    BottomInfoSection()
//}