package com.example.scrapuncle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.scrapuncle.auth.skeleton.PickupDetailSkeleton
import com.example.scrapuncle.auth.viewmodel.ScheduleViewModel
import com.example.scrapuncle.compoents.EmptyState
import com.example.scrapuncle.pages.schedule.formatAddress
import com.example.scrapuncle.pages.schedule.formatPickupDate
import com.example.scrapuncle.ui.theme.lightGreen
import com.example.scrapuncle.ui.theme.poppinsCategoryFont
import kotlinx.coroutines.delay


@Composable
fun PickupDetailScreen(
    pid: String,
    viewModel: ScheduleViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSkeleton by remember { mutableStateOf(true) }

     LaunchedEffect(Unit) {
        delay(1200)
        showSkeleton = false
    }

    val pickupWithAddress = uiState.pickups
        .firstOrNull { it.pickup.id == pid }

    when {
        uiState.isLoading  || showSkeleton  -> {
            PickupDetailSkeleton()
        }

        pickupWithAddress == null -> {
            EmptyState(
                message = "Pickup details not found",
                onBack = onBack
            )
        }

        else -> {
            val pickup = pickupWithAddress.pickup
            val address = formatAddress(pickupWithAddress.address)

            PickupDetailContent(
                pid = pickup.id,
                date = pickup.date,
                address = address,
                slot = pickup.slot,
                status = pickup.status,
                weight = pickup.weight,
                onBack = onBack
            )
        }
    }
}

@Composable
fun PickupDetailContent(
    pid: String,
    date: String,
    address: String,
    slot: String,
    status: String,
    weight: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(vertical = 18.dp, horizontal = 22.dp)
    ) {

        Text(
            text = "Your pickup is $status",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 22.sp,
            fontFamily = poppinsCategoryFont,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F1F1F),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Once verified, our pickup agent will reach you within the selected time slot.",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 13.sp,
            fontFamily = poppinsCategoryFont,
            color = Color.Black.copy(alpha = 0.65f),
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp, horizontal = 20.dp)

            ) {

                Image(
                    painter = painterResource(R.drawable.delivery_truck_img),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Fit
                )

                // -------- STATUS OVER IMAGE (TOP RIGHT) --------
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    DetailsStatusChip(status)
                }


            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.Start,
             ) {
                // -------- ADDRESS --------
                Text(
                    text = address,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = poppinsCategoryFont,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
                )


//                Spacer(Modifier.height(6.dp))
                Text(
                    text = "-------------------------------------",
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = poppinsCategoryFont,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray.copy(alpha = 0.72f),
                    textAlign = TextAlign.Center
                )




                InfoCardDetails(
                    title = "PICKUP DETAILS",
                ) {
                    Column(
//                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Spacer(Modifier.height(2.dp))

                        InfoRowDetails(
                            label = "Expected Weight",
                            value = weight
                        )
                        InfoRowDetails(
                            label = "Status",
                            value = status
                        )
                        InfoRowDetails(
                            label = "Pickup Date",
                            value = formatPickupDate(date)
                        )
                        InfoRowDetails(
                            label = "Time Slot",
                            value = slot
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }

        Spacer(Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.18f),
        ) {
            Button(
                onClick = {onBack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(44.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = lightGreen
                )
            ) {
                Text(
                    text = "Got it",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    fontFamily = poppinsCategoryFont,
                )
            }
        }
    }
}


@Composable
private fun InfoCardDetails(
    title: String,
    content: @Composable () -> Unit
) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsCategoryFont,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
                )
            }
            content()

}


@Composable
private fun InfoRowDetails(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = poppinsCategoryFont,
            color = Color.DarkGray.copy(alpha = 0.82f),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = poppinsCategoryFont,
            color = Color.Black.copy(alpha = 0.9f)
        )
    }
}
@Composable
fun DetailsStatusChip(status: String) {

    val (color, icon) = when (status) {
        "Completed" -> Color(0xFF2E7D32) to Icons.Default.CheckCircle
        "Cancelled" -> Color(0xFFC62828) to Icons.Default.Cancel
        else -> Color(0xFF1565C0) to Icons.Default.Schedule
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.8f),
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = Color.White.copy(alpha = 1f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(13.dp),
            tint = Color.Black.copy(alpha = 0.8f)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = status,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black.copy(alpha = 0.8f)
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PickupDetailScreenPreview(modifier: Modifier = Modifier) {
//    PickupDetailScreen(
//        pid = "76897979",
//        date = "2nd Dec, 2025",
//        address = "123 Green Street, Noida, UP",
//        slot = "11:30 AM - 1:30 PM",
//        status = "Pending",
//        weight = "50 Kgs",
//        onBack = { }
//    )
//}