package com.example.scrapuncle.pages.schedule

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.scrapuncle.R
import com.example.scrapuncle.auth.data.Address
import com.example.scrapuncle.auth.module.ScheduleEvent
import com.example.scrapuncle.auth.module.ScheduleUiEvent
import com.example.scrapuncle.auth.skeleton.AddressShimmer
import com.example.scrapuncle.auth.skeleton.rememberShimmerBrush
import com.example.scrapuncle.auth.viewmodel.ScheduleViewModel
import com.example.scrapuncle.ui.theme.lightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulePickupScreen(
    viewModel: ScheduleViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSelectAddress: () -> Unit
) {

    val state = viewModel.uiState.collectAsState().value


    // observe viewModel state
    val selectedSlot = state.selectedSlot
    val selectedWeight = state.selectedWeight
    val selectedAddress = state.addresses.firstOrNull { it.id == state.selectedAddressId }
    val selectedDate = state.selectedDate
    val isSubmitting = state.isSubmitting

    val context = LocalContext.current

    var showSheet by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
         viewModel.loadAddresses()
    }


    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is ScheduleUiEvent.PickupScheduled -> {
//                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    onBack() // navigate AFTER toast trigger
                }
                ScheduleUiEvent.NavigateBack -> onBack()
            }
        }
    }

//    LaunchedEffect(Unit) {
//        viewModel.uiEvents.collect { event ->
//            when (event) {
//                is ScheduleUiEvent.NavigateBack -> onBack()
//                else -> Unit
//            }
//        }
//    }

    if (showSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        LaunchedEffect(Unit) {
            sheetState.show()
        }
        AddressBottomSheet(
            addresses = state.addresses,
            selectedAddress = selectedAddress,
            onSelect = {
                viewModel.onEvent(ScheduleEvent.SelectAddress(it.id))
                showSheet = false
            },
            onAddNew = {
                showSheet = false
                onBack()
                onSelectAddress()
            },
            onClose = { showSheet = false }
        )
    }


    // ----------------------------- UI LAYOUT -----------------------------
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            //---------------- HEADER ----------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Black,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onBack() }
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Schedule Pickup",
                    fontWeight = FontWeight.SemiBold,
                    color = Black.copy(alpha = 0.94f),
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(2.dp))

            //---------------- FORM CONTENT ----------------
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp)
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // ------------------ DATE ------------------
                item {
                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_calendar),
                            contentDescription = null,
                            tint = Color.DarkGray.copy(alpha = 1.1f),
                            modifier = Modifier.size(20.dp)
                        )

                        Text(
                            text = "Date",
                            fontWeight = FontWeight.W500,
                            fontSize = 15.sp,
                            color = Color.DarkGray.copy(alpha = 1.4f)
                        )
                    }
                }

                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PickupDateSelector(
                            selectedDate = selectedDate,
                            onDateSelected = { viewModel.onEvent(ScheduleEvent.SelectDate(it)) }
                        )
                    }
                }

                // ------------------ TIME SLOT ------------------
                item {
                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_clock),
                            contentDescription = null,
                            tint = Color.DarkGray.copy(alpha = 1.1f),
                            modifier = Modifier.size(20.dp)
                        )

                        Text(
                            text = "Time",
                            fontWeight = FontWeight.W500,
                            fontSize = 15.sp,
                            color = Color.DarkGray.copy(alpha = 1.4f)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(2.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val timeSlots = listOf(
                            "11:30 AM to 1:30 PM",
                            "2:30 PM to 4:30 PM",
                            "4:30 PM to 7:00 PM"
                        )

                        timeSlots.forEach { slot ->
                            val isSelected = slot == selectedSlot
                            SingleSelectChip(
                                label = slot,
                                selected = isSelected,
                                onClick = {
                                    viewModel.onEvent(ScheduleEvent.SelectSlot(slot))
                                }
                            )
                        }
                    }
                }


                // ------------------ WEIGHT ------------------
                item {
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_weight),
                            contentDescription = null,
                            tint = Color.DarkGray.copy(alpha = 1.1f),
                            modifier = Modifier.size(20.dp)
                        )

                        Text(
                            text = "Estimated Weight",
                            fontWeight = FontWeight.W500,
                            fontSize = 15.sp,
                            color = Color.DarkGray.copy(alpha = 1.4f)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(2.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val weightRanges = listOf(
                            "0-10 Kgs",
                            "10-30 Kgs",
                            "30-50 Kgs",
                            "50-200 Kgs",
                            "More than 200 Kgs"
                        )

                        weightRanges.forEach { weight ->
                            val isSelected = weight == selectedWeight
                            SingleSelectChip(
                                label = weight,
                                selected = isSelected,
                                onClick = {
                                    viewModel.onEvent(ScheduleEvent.SelectWeight(weight))
                                }
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_location),
                            contentDescription = "Select Address",
                            tint = Color.DarkGray.copy(alpha = 1.1f),
                            modifier = Modifier.size(20.dp)
                        )

                        Text(
                            text = "Address",
                            fontWeight = FontWeight.W500,
                            fontSize = 15.sp,
                            color = Color.DarkGray.copy(alpha = 1.4f)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        if (selectedAddress != null) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        showSheet = true
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_pencil),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFF007AFF).copy(alpha = 0.78f)
                                )

                                Text(
                                    text = "Change",
                                    fontSize = 14.sp,
                                    color = Color(0xFF007AFF).copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }

                // ------------------ ADDRESS ------------------
                item {
                    Spacer(modifier = Modifier.height(2.dp))


                    val shimmerBrush = rememberShimmerBrush()

                    Crossfade(
                        targetState = state.isAddressLoading,
                        animationSpec = tween(
                            durationMillis = 350,
                            easing = FastOutSlowInEasing
                        ),
                        label = "AddressCrossfade"
                    ) { loading ->

                        when {
                            loading -> {
                                AddressShimmer(brush = shimmerBrush)
                            }

                            selectedAddress != null -> {
                                AddressCard(selectedAddress)
                            }

                            else -> {
                                CreateAddressButton(onSelectAddress)
                            }
                        }
                    }
                }

                //---------------- SUBMIT BUTTON ----------------
                item {
                    Spacer(modifier = Modifier.height(14.dp))

                    val canSchedule =
                        selectedAddress != null &&
                                selectedSlot != null &&
                                selectedWeight != null

                    Button(
                        onClick = {
                            val error = viewModel.validatePickup()

                            if (error != null) {
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            viewModel.onEvent(ScheduleEvent.SubmitPickup)
                        },
                        enabled = canSchedule,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .heightIn(min = 48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00A651)
                        )
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(24.dp)
                            )
                        } else {
                            Text(
                                "SCHEDULE PICKUP",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun SingleSelectChip(
    label: String, selected: Boolean, onClick: () -> Unit
) {

    val scale by animateFloatAsState(
        targetValue = if (selected) 1.055f else 1.01f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "chipScale"
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .background(
                brush = if (selected)
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
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onClick()
            }
            .padding(horizontal = 12.dp, vertical = 6.dp)) {
        Text(
            text = label,
            color = if (selected) Color.White else Color.DarkGray.copy(alpha = 9f),
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp
        )
    }
}


@Composable
fun AddressCard(
    address: Address
) {

    val formattedAddress = buildString {
        append(address.fullAddress)
        if (address.city.isNotEmpty()) append(", ${address.city}")
        if (address.state.isNotEmpty()) append(", ${address.state}")
        if (address.pinCode.isNotEmpty()) append(" - ${address.pinCode}")
    }

    val backgroundColor =
        Color(0xFF4CAF50).copy(alpha = 0.05f)

    val borderColor =
          Color(0xFF4CAF50).copy(alpha = 0.35f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(14.dp)
    ) {
        Column {

            // Address
            Text(
                text = formattedAddress,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF212121),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Pincode ${address.pinCode}",
                    fontSize = 12.sp,
                    color = Color(0xFF757575),
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFDFF5E4),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Operational",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }
    }
}



@Composable
fun CreateAddressButton(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .heightIn(min = 36.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White
            ),
            border = BorderStroke(1.dp, Color(0xFF00A651))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Filled.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF00A651)
                )
                Text("Create Address", color = Color(0xFF00A651))
            }
        }
    }
}
