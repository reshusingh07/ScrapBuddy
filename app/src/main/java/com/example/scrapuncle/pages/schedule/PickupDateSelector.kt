package com.example.scrapuncle.pages.schedule

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrapuncle.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupDateSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {


    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
    val scrollState = rememberScrollState()
    val today = LocalDate.now()
    val next7Days = (0..6).map { today.plusDays(it.toLong()) }



    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        //  Use LazyRow for horizontal scroll
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(0.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(next7Days) { date ->
                val isSelected = date == selectedDate
                //  Smoothly animate the background and text color
//                val backgroundColor by animateColorAsState(
//                    targetValue = if (isSelected) Color(0xFF00A651).copy(alpha = 0.82f)
//                    else Color.Transparent,
//                    label = "background"
//                )


                //  Add a subtle scaling animation when selected
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.055f else 1f,
                    label = "scale"
                )

                Box(
                    modifier = Modifier
                        .width(62.dp)
                        .height(62.dp)
                        .scale(scale) // smooth size transition
                        .background(
                            brush = if (isSelected)
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF00A651).copy(alpha = 0.75f),
                                        Color(0xFF00A651).copy(alpha = 0.9f) // solid color as gradient
                                    )
                                )
                            else
                                Brush.verticalGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.1f),
                                        Color.LightGray.copy(alpha = 0.14f)
                                    )
                                ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.LightGray.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onDateSelected(date) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = date.dayOfWeek.name.take(3)
                                .lowercase()
                                .replaceFirstChar { it.uppercase() },
                            color = if (isSelected) Color.White else Color.Black,
                            fontSize = 13.sp
                        )
                        Text(
                            text = date.dayOfMonth.toString(),
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color.DarkGray.copy(alpha = 9f)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        DateDisplayCard(selectedDate = selectedDate.format(formatter))
    }
}


@Composable
fun DateDisplayCard(selectedDate: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp)
            .background(
                Color.LightGray.copy(alpha = 0.1f),
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = Color.LightGray.copy(alpha = 0.2f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 15.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedDate,
                color = Color.DarkGray.copy(alpha = 0.7f),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
               painter = painterResource(id = R.drawable.icon_calendar_today),
                contentDescription = null,
                tint =  Color.DarkGray.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
