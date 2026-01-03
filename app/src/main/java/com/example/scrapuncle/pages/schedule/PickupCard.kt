package com.example.scrapuncle.pages.schedule

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrapuncle.auth.data.Address
import com.example.scrapuncle.ui.theme.InterFontFamily
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun PickupCard(
    dateText: String,
    address: String,
    pid: String,
    slot: String,
    status: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {


    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val cardShape = RoundedCornerShape(12.dp)


    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        label = "CardPressScale"
    )


    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 5.dp)
            .border(
                width = 1.dp,
                color = Color.Transparent
            )

            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
            pressedElevation = 4.dp
        )

    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            /* ---------- DATE + STATUS ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatPickupDate(dateText),
                    fontFamily = InterFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F1F1F)
                )

                Spacer(Modifier.weight(1f))

                StatusChip(status)
            }

            /* ---------- ADDRESS ---------- */
            Text(
                text = address,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 13.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black.copy(alpha = 0.7f)
            )

            // ---------- SLOT ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // LEFT CONTENT
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    // Time slot row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = Color(0xFF6B6B6B)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = slot,
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            color = Color.Black.copy(alpha = 0.6f)
                        )
                    }

                    // PID
                    Text(
                        text = " PID • $pid",
                        fontFamily = InterFontFamily,
                        fontSize = 11.sp,
                        color = Color.Black.copy(alpha = 0.45f)
                    )
                }

                // RIGHT SINGLE ICON
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View pickup details",
                    modifier = Modifier
                        .size(22.dp)
                        .alpha(0.6f)
                )
            }

        }
    }
}

/* ---------------------------------------------------
   STATUS CHIP — OUTLINED & MINIMAL
--------------------------------------------------- */

@Composable
fun StatusChip(status: String) {

    val (color, icon) = when (status) {
        "Completed" -> Color(0xFF2E7D32) to Icons.Default.CheckCircle
        "Cancelled" -> Color(0xFFC62828) to Icons.Default.Cancel
        else -> Color(0xFF1565C0) to Icons.Default.Schedule
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 0.dp, // remove border
                color = Color.Transparent
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(13.dp),
            tint = color
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = status,
            fontFamily = InterFontFamily,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PickupCardPreview() {
//    Column {
//        PickupCard(
//            dateText = "02nd Dec, 2025",
//            address = "123 Green Street, Noida, Uttar Pradesh, 201301, INDIA",
//            pid = "PKP123456",
//            slot = "11:30 AM to 1:30 PM",
//            status = "Pending",
//            onClick = {}
//        )
//        PickupCard(
//            dateText = "02nd Dec, 2025",
//            address = "123 Green Street, Noida, Uttar Pradesh, 201301, INDIA",
//            pid = "PKP123456",
//            slot = "11:30 AM to 1:30 PM",
//            status = "Pending",
//            onClick = {}
//        )
//        PickupCard(
//            dateText = "02nd Dec, 2025",
//            address = "123 Green Street, Noida, Uttar Pradesh, 201301, INDIA",
//            pid = "PKP123456",
//            slot = "11:30 AM to 1:30 PM",
//            status = "Pending",
//            onClick = {}
//
//        )
//    }
//}


fun formatPickupDate(dateString: String): String {
    return try {
        val date = LocalDate.parse(dateString)
        val today = LocalDate.now()

        when {
            date.isEqual(today) -> "Today"
            date.isEqual(today.plusDays(1)) -> "Tomorrow"
            date.isEqual(today.minusDays(1)) -> "Yesterday"
            else -> {
                val day = date.dayOfMonth
                val suffix = when {
                    day in 11..13 -> "th"
                    day % 10 == 1 -> "st"
                    day % 10 == 2 -> "nd"
                    day % 10 == 3 -> "rd"
                    else -> "th"
                }

                val month = date.format(
                    DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH)
                )

                "$day$suffix $month, ${date.year}"
            }
        }
    } catch (_: Exception) {
        dateString
    }
}

fun formatAddress(address: Address?): String {
    if (address == null) return "Address is showing in few sec"
    return buildString {

        append(address.fullAddress)
        if (address.city.isNotEmpty()) append(", ${address.city}")
        if (address.state.isNotEmpty()) append(", ${address.state}")
        if (address.pinCode.isNotEmpty()) append(", ${address.pinCode}")
        append(", INDIA")
    }
}
