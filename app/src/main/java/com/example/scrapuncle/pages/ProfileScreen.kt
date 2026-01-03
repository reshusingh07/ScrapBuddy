package com.example.scrapuncle.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.scrapuncle.R
import com.example.scrapuncle.auth.viewmodel.AccountSettingsViewModel
import com.example.scrapuncle.compoents.AppToast
import com.example.scrapuncle.ui.theme.InterFontFamily
import com.example.scrapuncle.ui.theme.lightGreen


@Composable
fun ProfileScreen(
    onNavigateToAccountSetting: () -> Unit,
    onNavigateToAboutUs: () -> Unit,
    viewModel: AccountSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val initials = getInitials(uiState.fullName)
    var toastData by remember {
        mutableStateOf<Pair<String, Int?>?>(null)
    }




    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
//            .background(Color(0xFFF8F8F8))
        ) {


            // --------------------- Header ---------------------

            // --------------------- Profile Section ---------------------
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 5.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(6.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(lightGreen.copy(alpha = 0.15f))
                        .padding(start = 5.dp, end = 8.dp, top = 5.dp, bottom = 5.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onNavigateToAccountSetting()
                        }

                ) {

                    // Avatar Circle
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00A651))
                    ) {
                        Text(
                            text = initials,
                            fontFamily = InterFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(3.dp)
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.icon_arrow_right_ios),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(90f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Hi ${uiState.fullName} !",
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = formatPhoneNumber(uiState.phone),
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(56.dp))


            // --------------------- Options List ---------------------
            MenuCard(
                title = "Help & Support", icon = R.drawable.icon_question,
                onClick = {
                    toastData =
                        "Under the construction!" to R.drawable.icon_it_support
                })
            Spacer(modifier = Modifier.height(2.dp))
            MenuCard(
                title = "Terms & Conditions", icon = R.drawable.icon_paper,
                onClick = {
                    toastData =
                        "Under the construction!" to R.drawable.icon_it_support
                })

            Spacer(modifier = Modifier.height(16.dp))

            MenuCard(
                title = "Account Settings",
                icon = R.drawable.icon_setting,
                onClick = { onNavigateToAccountSetting() })
            Spacer(modifier = Modifier.height(2.dp))
            MenuCard(
                title = "About Us",
                icon = R.drawable.icon_about_us,
                onClick = { onNavigateToAboutUs() })

            Spacer(modifier = Modifier.height(16.dp))

            MenuCard(
                title = "Check for Updates", icon = R.drawable.icon_update,
                onClick = {
                    toastData =
                        "You're already on the latest version!" to R.drawable.icon_checked_update
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --------------------- Footer ---------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "ScrapUncle",
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = Color(0xFFA8A7A7)
                )
                Text(
                    text = " Version 6.1.23",
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(12.dp))

            }
            // ================= TOAST OVERLAY ====================

        }
        toastData?.let { (message, iconRes) ->
            Box(
                modifier = Modifier
//                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
//                    .statusBarsPadding()
            ) {
                AppToast(
                    message = message,
                    iconRes = iconRes
                )
            }
        }
    }
}


@Composable
fun MenuCard(title: String, icon: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 1.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.LightGray.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 15.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = Color(0xFF6A6A6A).copy(alpha = 0.9f),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                color = Color(0xFF333333).copy(alpha = 0.85f),
                fontWeight = FontWeight.W400
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_right_ios),
                contentDescription = "Next",
                tint = Color.Gray,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

fun getInitials(fullName: String): String {
    val parts = fullName
        .trim()
        .split("\\s+".toRegex()) // splits multiple spaces safely

    if (parts.isEmpty()) return ""

    val first = parts.first().firstOrNull()?.uppercaseChar() ?: return ""
    val last = parts.last().firstOrNull()?.uppercaseChar() ?: first

    return "$first$last"
}

fun formatPhoneNumber(phone: String): String {
    return if (phone.startsWith("+91") && phone.length > 3) {
        "+91 ${phone.substring(3)}"
    } else {
        phone
    }
}
