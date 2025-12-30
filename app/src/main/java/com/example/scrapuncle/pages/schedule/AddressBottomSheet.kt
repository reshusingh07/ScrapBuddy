package com.example.scrapuncle.pages.schedule

import android.R.attr.fontWeight
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrapuncle.auth.data.Address
import com.example.scrapuncle.ui.theme.lightBlack
import com.example.scrapuncle.ui.theme.poppinsCategoryFont
import com.google.common.math.LinearTransformation.horizontal


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressBottomSheet(
    addresses: List<Address>,
    selectedAddress: Address?,
    onSelect: (Address) -> Unit,
    onAddNew: () -> Unit,
    onClose: () -> Unit
) {

    val windowInfo = LocalWindowInfo.current
    val screenHeight = with(LocalDensity.current) {
        windowInfo.containerSize.height.toDp()
    }

    // smooth animation
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 20.dp)
    ) {
        val maxSheetHeight = screenHeight * 0.5f

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = maxSheetHeight)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Select Address",
                            fontFamily = poppinsCategoryFont,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray.copy(alpha = 0.9f),
                        )
                    }
                }

                // Address Cards

                items(addresses) { addr ->
                    AddressCardSheet(
                        address = addr,
                        isSelected = selectedAddress?.id == addr.id,
                        onClick = { onSelect(addr) }
                    )
                Spacer(modifier = Modifier.height(12.dp))
                }



                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                    // Add New Address Button
                    Button(
                        onClick = onAddNew,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(horizontal = 30.dp)
                            .heightIn(min = 42.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White
                        ),
                        border = BorderStroke((1).dp, Color(0xFF00A651)),
                        contentPadding = PaddingValues(
                            horizontal = 12.dp,
                            vertical = 6.dp // small padding for compact look
                        )
                    ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Color(0xFF00A651)
                                )

                                Text("Add New Address", color = Color(0xFF00A651))
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
//}




@Composable
fun AddressCardSheet(
    address: Address,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.03f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = ""
    )

    // FORMAT THE ADDRESS EXACTLY LIKE YOU WANT
    val formattedAddress = buildString {
        append(address.fullAddress)

        if (address.city.isNotEmpty()) append(", ${address.city}")
        if (address.state.isNotEmpty()) append(", ${address.state}")
        if (address.pinCode.isNotEmpty()) append(", ${address.pinCode}")
        append(", India")
    }

    Box(
        modifier = Modifier
            .scale(scale)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE8F5E9))      // Light green
            .border(
                1.dp,
                if (isSelected) Color(0xFF00A651) else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(14.dp)
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {

            // TAG ROW
            Row(verticalAlignment = Alignment.CenterVertically) {

                // HOME TAG
                Box(
                    modifier = Modifier
                        .background(
                            Color.White.copy(alpha = 0.6f),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        address.tag.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray.copy(alpha = 0.9f)
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))

                // OPERATIONAL TAG
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFFD0F8CE), RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "OPERATIONAL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // FULL FORMATTED ADDRESS
            Text(
                formattedAddress,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {

                Text(
                    text = "PINCODE: ",
                    fontSize = 11.sp,
                    color = Color.DarkGray.copy(alpha = 0.7f)
                )
                // PIN CODE LINE
                Text(
                    text = address.pinCode,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray.copy(alpha = 0.9f)
                )
            }
        }
    }
}
