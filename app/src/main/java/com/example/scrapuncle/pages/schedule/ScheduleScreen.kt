package com.example.scrapuncle.pages.schedule

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.scrapuncle.R
import com.example.scrapuncle.auth.skeleton.ShimmerPickupCardItem
import com.example.scrapuncle.auth.skeleton.rememberShimmerBrush
import com.example.scrapuncle.auth.viewmodel.ScheduleViewModel


private enum class UiMode {
    Loading, Empty, Content
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = hiltViewModel(), onScheduleNow: () -> Unit
) {
    val state = viewModel.uiState.collectAsState().value
    val items = state.pickups

    val uiMode = when {
        state.isLoading -> UiMode.Loading
        items.isEmpty() -> UiMode.Empty
        else -> UiMode.Content
    }

    val shimmerBrush = rememberShimmerBrush()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 4.dp)
    ) {
        // FIXED TITLE AT TOP
        RateHeader()

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            Crossfade(targetState = uiMode,
                label = "PickupListCrossfade"
                ) { mode ->


                when(mode) {

                    UiMode.Loading -> {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 90.dp),

                            ) {
                            items(4) {

                                ShimmerPickupCardItem(brush = shimmerBrush)
                            }
                        }
                    }


                    UiMode.Empty -> {
                        EmptyPickupScreen()
                    }

                    UiMode.Content -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
//                                .background(Color(0xFFF7F8FA)) // soft neutral background
                        ) {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 80.dp),
                        ) {
                            items(items) { item ->
                                val pickup = item.pickup
                                val formatted = formatAddress(item.address)

                                // animate placement so item changes are smooth
                                PickupCard(
                                    dateText = pickup.date,
                                    address = formatted,
                                    pid = pickup.id,
                                    slot = pickup.slot,
                                    status = pickup.status
                                )
                            }
                        }
                        }
                    }
                }
            }

            // FAB Button
            BookNowButton(
                onClick = onScheduleNow,
                modifier = Modifier
                    .align(Alignment.BottomEnd) // NOW VALID
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun BookNowButton(
    onClick: () -> Unit, modifier: Modifier = Modifier
) {


    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        containerColor = Color.Black.copy(alpha = 0.9f),
        contentColor = Color.White,
        modifier = modifier.size(60.dp)  // works ONLY inside outer Box
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Book Now",
            modifier = Modifier.size(28.dp)
        )
    }
}


@Composable
fun EmptyPickupScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_empty_trash),
            contentDescription = "Book Now",
            modifier = Modifier.size(140.dp)
        )

        Text(
            text = "No pickups found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Schedule your first pickup to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun RateHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
    ) {
        Text(
            text = "Pickup",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black.copy(alpha = 0.8f)
        )
        Text(
            text = "View or schedule your scrap pickup",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}


